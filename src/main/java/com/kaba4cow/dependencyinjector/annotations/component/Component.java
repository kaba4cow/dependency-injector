package com.kaba4cow.dependencyinjector.annotations.component;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import com.kaba4cow.dependencyinjector.ApplicationContext;

/**
 * This annotation marks classes as components in the application that should be managed by the {@link ApplicationContext}.
 * Components are automatically instantiated and injected with dependencies.
 */
@Documented
@Retention(RUNTIME)
@Target(TYPE)
public @interface Component {

}
