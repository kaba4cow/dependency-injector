package com.kaba4cow.dependencyinjector;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.reflections.Reflections;

class ReflectionHelper {

	private ReflectionHelper() {}

	public static Set<Class<?>> getTypes(String packageName, Class<? extends Annotation> annotation) {
		return new Reflections(packageName).getTypesAnnotatedWith(annotation);
	}

	public static List<Field> getFields(Class<?> type, Class<? extends Annotation> annotation) {
		List<Field> fields = new ArrayList<>();
		for (Field field : type.getDeclaredFields())
			if (field.isAnnotationPresent(annotation))
				fields.add(field);
		return fields;
	}

	public static boolean hasFields(Class<?> type, Class<? extends Annotation> annotation) {
		for (Field field : type.getDeclaredFields())
			if (field.isAnnotationPresent(annotation))
				return true;
		return false;
	}

	public static List<Constructor<?>> getConstructors(Class<?> type, Class<? extends Annotation> annotation) {
		List<Constructor<?>> constructors = new ArrayList<>();
		for (Constructor<?> constructor : type.getDeclaredConstructors())
			if (constructor.isAnnotationPresent(annotation))
				constructors.add(constructor);
		return constructors;
	}

	public static boolean hasConstructors(Class<?> type, Class<? extends Annotation> annotation) {
		for (Constructor<?> constructor : type.getDeclaredConstructors())
			if (constructor.isAnnotationPresent(annotation))
				return true;
		return false;
	}

	public static List<Method> getMethods(Class<?> type, Class<? extends Annotation> annotation) {
		List<Method> methods = new ArrayList<>();
		for (Method method : type.getDeclaredMethods())
			if (method.isAnnotationPresent(annotation))
				methods.add(method);
		return methods;
	}

	public static boolean hasMethods(Class<?> type, Class<? extends Annotation> annotation) {
		for (Method method : type.getDeclaredMethods())
			if (method.isAnnotationPresent(annotation))
				return true;
		return false;
	}

}
