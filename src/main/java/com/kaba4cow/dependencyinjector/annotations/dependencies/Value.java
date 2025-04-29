package com.kaba4cow.dependencyinjector.annotations.dependencies;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * Injects a configuration value from either:
 * <ul>
 * <li>a configuration file</li>
 * <li>a {@link Value}-annotated method inside a {@link Configuration} class</li>
 * </ul>
 * into a field, parameter, or method.
 * <p>
 * Can be used on:
 * <ul>
 * <li>Fields — to inject a value directly into a component or bean</li>
 * <li>Parameters — for constructors, methods, or {@link Bean} methods</li>
 * <li>Methods in {@code @Configuration} classes — to define named values programmatically</li>
 * </ul>
 * </p>
 * <p>
 * The {@code value} attribute specifies the key to look up. If the key is present in both file and config-class sources, the
 * config-class value has priority.
 * </p>
 */
@Documented
@Retention(RUNTIME)
@Target({ FIELD, PARAMETER, METHOD })
public @interface Value {

	/**
	 * Specifies the key for the configuration value to inject.
	 *
	 * @return the configuration key
	 */
	public String value();

}
