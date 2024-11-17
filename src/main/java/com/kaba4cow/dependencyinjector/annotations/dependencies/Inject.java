package com.kaba4cow.dependencyinjector.annotations.dependencies;

import static java.lang.annotation.ElementType.CONSTRUCTOR;
import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import com.kaba4cow.dependencyinjector.ApplicationContext;

/**
 * This annotation marks constructor parameters or fields as dependencies to be injected by the {@link ApplicationContext}.
 * Fields or constructor parameters annotated with {@link Inject} will be automatically injected with the corresponding
 * component.
 */
@Documented
@Retention(RUNTIME)
@Target({ FIELD, CONSTRUCTOR })
public @interface Inject {

}
