package com.kaba4cow.dependencyinjector.annotations.dependencies;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * This annotation is used to inject configuration values into fields in components. The value to inject is specified by the
 * `value` attribute.
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
