package com.kaba4cow.dependencyinjector.annotations.scheduler;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import java.util.concurrent.TimeUnit;

/**
 * This annotation is used to mark methods for scheduling tasks at fixed intervals. This annotation provides a way to define
 * methods that should be executed periodically based on the provided timing parameters. The method annotated with
 * {@code @Scheduled} will be executed with a fixed delay after the completion of its previous execution. The delay and initial
 * delay are specified through the annotation's parameters.
 */
@Documented
@Retention(RUNTIME)
@Target(METHOD)
public @interface Scheduled {

	/**
	 * Defines the initial delay before the first execution of the scheduled task.
	 * 
	 * @return the initial delay before the first task execution
	 */
	long initialDelay();

	/**
	 * Defines the fixed delay between consecutive executions of the scheduled task.
	 * 
	 * @return the delay between consecutive executions
	 */
	long fixedDelay();

	/**
	 * Defines the time unit for the {@code initialDelay} and {@code fixedDelay} values.
	 * 
	 * @return the time unit for the delay values
	 */
	TimeUnit timeUnit();

}
