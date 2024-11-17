package com.kaba4cow.dependencyinjector.annotations.component;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * This annotation marks a method to be invoked after the component's dependencies have been injected and the component has been
 * constructed. The method is invoked once during the initialization phase.
 */
@Documented
@Retention(RUNTIME)
@Target(METHOD)
public @interface PostConstruct {

}
