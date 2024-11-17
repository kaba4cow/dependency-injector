package com.kaba4cow.dependencyinjector.annotations.component;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * This annotation marks a method to be invoked before the component is destroyed, typically during application shutdown.
 */
@Documented
@Retention(RUNTIME)
@Target(METHOD)
public @interface PreDestroy {

}
