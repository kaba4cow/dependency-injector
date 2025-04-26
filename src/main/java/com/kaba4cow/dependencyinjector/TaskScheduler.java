package com.kaba4cow.dependencyinjector;

import java.lang.reflect.Method;
import java.util.Set;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import com.kaba4cow.dependencyinjector.annotations.component.Lazy;
import com.kaba4cow.dependencyinjector.annotations.scheduler.Scheduled;

class TaskScheduler {

	private final ScheduledExecutorService scheduler;

	TaskScheduler(ApplicationContext context, Set<Class<?>> types) {
		scheduler = Executors.newScheduledThreadPool(Runtime.getRuntime().availableProcessors());
		for (Class<?> type : types)
			if (!type.isAnnotationPresent(Lazy.class) || !ReflectionHelper.getMethods(type, Scheduled.class).isEmpty()) {
				Object component = context.getComponent(type);
				for (Method method : ReflectionHelper.getMethods(type, Scheduled.class)) {
					Scheduled annotation = method.getAnnotation(Scheduled.class);
					long fixedDelay = annotation.fixedDelay();
					if (fixedDelay < 1L)
						throw new IllegalArgumentException(
								String.format("Scheduled method %s in class %s must have a fixedDelay > 0", method.getName(),
										component.getClass().getName()));
					long initialDelay = annotation.initialDelay() > 0L ? annotation.initialDelay() : 0L;
					TimeUnit timeUnit = annotation.timeUnit();
					scheduler.scheduleWithFixedDelay(new Task(component, method), initialDelay, fixedDelay, timeUnit);
				}
			}
	}

	void shutdown() {
		scheduler.shutdown();
		try {
			if (!scheduler.awaitTermination(1L, TimeUnit.SECONDS))
				scheduler.shutdownNow();
		} catch (InterruptedException exception) {
			scheduler.shutdownNow();
			Thread.currentThread().interrupt();
		}
	}

	private static class Task implements Runnable {

		private final Object component;
		private final Method method;

		private Task(Object component, Method method) {
			this.component = component;
			this.method = method;
		}

		@Override
		public void run() {
			try {
				method.setAccessible(true);
				method.invoke(component);
			} catch (Exception exception) {
				exception.printStackTrace();
			}
		}

	}

}
