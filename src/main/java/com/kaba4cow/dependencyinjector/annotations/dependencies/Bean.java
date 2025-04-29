package com.kaba4cow.dependencyinjector.annotations.dependencies;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import com.kaba4cow.dependencyinjector.ApplicationContext;

/**
 * Marks a method inside a {@link Configuration} class as a factory for a component (bean). The method's return value is
 * registered as a component in the {@link ApplicationContext}. Only one bean of a given type can be registered. Attempting to
 * register multiple beans of the same return type will result in a runtime error.
 */
@Documented
@Retention(RUNTIME)
@Target(METHOD)
public @interface Bean {

}
