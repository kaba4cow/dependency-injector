package com.kaba4cow.dependencyinjector.annotations.dependencies;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * Indicates that a class provides configuration methods for the application context.
 * <p>
 * A {@code @Configuration} class may contain:
 * <ul>
 * <li>{@link Bean} methods — to define and register components in the context</li>
 * <li>{@link Value} methods — to supply configuration values by key</li>
 * </ul>
 * These methods will be invoked during context initialization.
 * </p>
 */
@Documented
@Retention(RUNTIME)
@Target(TYPE)
public @interface Configuration {

}
