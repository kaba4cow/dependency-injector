package com.kaba4cow.dependencyinjector.annotations.component;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * This annotation is used to mark a component for lazy initialization. A component marked with this annotation will not be
 * created until it is explicitly required by another component or service. This annotation can be useful for optimizing
 * application startup time, deferring the creation of unnecessary objects until they are actually needed.
 */
@Documented
@Retention(RUNTIME)
@Target(TYPE)
public @interface Lazy {

}
