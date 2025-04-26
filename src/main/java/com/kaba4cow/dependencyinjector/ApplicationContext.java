package com.kaba4cow.dependencyinjector;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Supplier;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.kaba4cow.dependencyinjector.annotations.component.Component;
import com.kaba4cow.dependencyinjector.annotations.component.PostConstruct;
import com.kaba4cow.dependencyinjector.annotations.component.PreDestroy;
import com.kaba4cow.dependencyinjector.annotations.dependencies.Inject;
import com.kaba4cow.dependencyinjector.annotations.dependencies.Value;
import com.kaba4cow.dependencyinjector.configtools.ConfigLoaderFactory;

/**
 * This class manages the application context, handling component lifecycle, configuration loading, and dependency injection.
 */
public class ApplicationContext {

	private static final Logger log = LoggerFactory.getLogger("ApplicationContext");

	private final Map<String, Object> config;
	private final Map<Class<?>, ComponentSupplier<?>> components;

	private final TaskScheduler scheduler;

	/**
	 * Initializes the application context by reading the configuration file and scanning the specified package for components.
	 *
	 * @param basePackage the base package to scan for components
	 * @param configFile  the configuration file path or {@code null}
	 * 
	 * @throws Exception if an error occurs during initialization
	 */
	public ApplicationContext(String basePackage, String configFile) throws Exception {
		if (Objects.nonNull(configFile)) {
			log.info(String.format("Reading config file %s", configFile));
			try {
				config = ConfigLoaderFactory.getConfigLoader(configFile).loadConfig(configFile);
			} catch (Exception exception) {
				throw new RuntimeException("Could not read config", exception);
			}
			log.info("Config file read successfully");
		} else
			config = new HashMap<>();
		components = new HashMap<>();
		log.info(String.format("Scanning package %s for components", basePackage));
		Set<Class<?>> types = ReflectionHelper.getTypes(basePackage, Component.class);
		log.info(String.format("Package scanned successfully, %s components retrieved", types.size()));
		for (Class<?> type : types) {
			components.put(type, new ComponentSupplier<>(type));
			log.info(String.format("Registered component of type %s", type.getName()));
		}
		scheduler = new TaskScheduler(this, types);
		Runtime.getRuntime().addShutdownHook(new Thread(this::close, "ApplicationContextShutdownHook"));
	}

	private Constructor<?> getConstructor(Class<?> type) throws Exception {
		if (ReflectionHelper.hasConstructors(type, Inject.class))
			return ReflectionHelper.getConstructors(type, Inject.class).get(0);
		return type.getConstructor();
	}

	private void injectFields(Object component) throws Exception {
		for (Field field : ReflectionHelper.getFields(component.getClass(), Inject.class)) {
			field.setAccessible(true);
			field.set(component, getComponent(field.getType()));
		}
		for (Field field : ReflectionHelper.getFields(component.getClass(), Value.class)) {
			field.setAccessible(true);
			field.set(component, getConfigValue(field.getType(), field.getAnnotation(Value.class).value()));
		}
	}

	private void invokePostConstruct(Object component) {
		Class<?> type = component.getClass();
		Map<String, Method> invoked = new ConcurrentHashMap<>();
		while (type != null) {
			for (Method method : ReflectionHelper.getMethods(type, PostConstruct.class)) {
				String signature = method.getName() + Arrays.toString(method.getParameterTypes());
				if (!invoked.containsKey(signature))
					try {
						method.setAccessible(true);
						method.invoke(component, injectDependencies(method.getParameters()));
						invoked.put(signature, method);
					} catch (Exception exception) {
						throw new RuntimeException(String.format("Failed to invoke post construct method %s on %s",
								method.getName(), type.getName()), exception);
					}
			}
			type = type.getSuperclass();
		}
		log.info(String.format("Component of type %s constructed successfully", component.getClass().getName()));
	}

	private void invokePreDestroy(Object component) {
		Class<?> type = component.getClass();
		Map<String, Method> invoked = new ConcurrentHashMap<>();
		while (type != null) {
			for (Method method : ReflectionHelper.getMethods(type, PreDestroy.class)) {
				String signature = method.getName() + Arrays.toString(method.getParameterTypes());
				if (!invoked.containsKey(signature))
					try {
						method.setAccessible(true);
						method.invoke(component, injectDependencies(method.getParameters()));
						invoked.put(signature, method);
					} catch (Exception exception) {
						throw new RuntimeException(
								String.format("Failed to invoke pre destroy method %s on %s", method.getName(), type.getName()),
								exception);
					}
			}
			type = type.getSuperclass();
		}
		log.info(String.format("Component of type %s destroyed successfully", component.getClass().getName()));
	}

	private Object[] injectDependencies(Parameter[] parameters) {
		Object[] arguments = new Object[parameters.length];
		for (int i = 0; i < parameters.length; i++)
			if (parameters[i].isAnnotationPresent(Value.class))
				arguments[i] = getConfigValue(parameters[i].getType(), parameters[i].getAnnotation(Value.class).value());
			else
				arguments[i] = getComponent(parameters[i].getType());
		return arguments;
	}

	/**
	 * Closes the application context by shutting down the scheduler and invoking pre-destroy methods on components.
	 */
	public void close() {
		scheduler.shutdown();
		for (ComponentSupplier<?> component : components.values())
			if (component.initialized())
				try {
					invokePreDestroy(component.get());
				} catch (Exception exception) {
					exception.printStackTrace();
				}
		config.clear();
		components.clear();
	}

	/**
	 * Retrieves a component of the specified type from the application context.
	 *
	 * @param type the component type
	 * @param <T>  the component type
	 * 
	 * @return the component instance
	 * 
	 * @throws RuntimeException if no component of the specified type is found
	 */
	@SuppressWarnings("unchecked")
	public <T> T getComponent(Class<T> type) {
		if (!components.containsKey(type))
			throw new RuntimeException(String.format("Found no component of type %s", type.getName()));
		return (T) components.get(type).get();
	}

	/**
	 * Retrieves a component of the specified type from the application context, wrapped in an {@link Optional}.
	 *
	 * @param type the component type
	 * @param <T>  the component type
	 * 
	 * @return an {@link Optional} containing the component instance, or empty if not found
	 */
	public <T> Optional<T> optComponent(Class<T> type) {
		return Optional.ofNullable(getComponent(type));
	}

	/**
	 * Retrieves a configuration value of the specified type from the application context.
	 *
	 * @param type the type of the configuration value
	 * @param key  the configuration key
	 * @param <T>  the type of the configuration value
	 * 
	 * @return the configuration value
	 */
	@SuppressWarnings("unchecked")
	public <T> T getConfigValue(Class<T> type, String key) {
		return (T) parseValue(type, config.get(key));
	}

	/**
	 * Retrieves a configuration value of the specified type from the application context, wrapped in an {@link Optional}.
	 *
	 * @param type the type of the configuration value
	 * @param key  the configuration key
	 * @param <T>  the type of the configuration value
	 * 
	 * @return an {@link Optional} containing the configuration value, or empty if not found
	 */
	public <T> Optional<T> optConfigValue(Class<T> type, String key) {
		return Optional.ofNullable(getConfigValue(type, key));
	}

	private Object parseValue(Class<?> type, Object value) {
		if (Objects.isNull(value))
			return null;
		else if (type.isEnum()) {
			for (Object constant : type.getEnumConstants())
				if (Objects.equals(constant.toString(), value))
					return constant;
			throw new IllegalArgumentException(String.format("No enum of type %s found for value %s", type.getName(), value));
		} else if (type == List.class)
			return value;
		else if (type == int.class || type == Integer.class)
			return Integer.parseInt(value.toString());
		else if (type == long.class || type == Long.class)
			return Long.parseLong(value.toString());
		else if (type == float.class || type == Float.class)
			return Float.parseFloat(value.toString());
		else if (type == double.class || type == Double.class)
			return Double.parseDouble(value.toString());
		else if (type == boolean.class || type == Boolean.class)
			return Boolean.parseBoolean(value.toString());
		else if (type == String.class)
			return value;
		throw new IllegalArgumentException(String.format("Unsupported value type %s", type.getName()));
	}

	private class ComponentSupplier<T> implements Supplier<T> {

		private final Class<T> type;
		private T instance;

		private ComponentSupplier(Class<T> type) {
			this.type = type;
			this.instance = null;
		}

		@SuppressWarnings("unchecked")
		@Override
		public T get() {
			if (!initialized())
				try {
					Constructor<?> constructor = getConstructor(type);
					log.info(String.format("Creating component of type %s", type.getName()));
					instance = (T) constructor.newInstance(injectDependencies(constructor.getParameters()));
					log.info(String.format("Component of type %s initialized successfully", type.getName()));
					injectFields(instance);
					invokePostConstruct(instance);
					log.info(String.format("Component of type %s created successfully", type.getName()));
				} catch (Exception exception) {
					throw new RuntimeException(String.format("Failed to create component %s", type.getName()), exception);
				}
			return instance;
		}

		private boolean initialized() {
			return Objects.nonNull(instance);
		}

	}

}
