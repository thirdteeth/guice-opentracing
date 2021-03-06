package org.thirdteeth.guice.opentracing;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import java.lang.annotation.ElementType;
import java.lang.annotation.RetentionPolicy;

/**
 * Specifies whether or not the type/method should be traced. When used at the type, all non-annotated methods will
 * inherit its configuration. When used at the method, the annotation on the type (if any) is ignored.
 *
 * When used at the type, marks all methods on the type for tracing by the interceptor. When used at the method,
 * marks the method for tracing by the interceptor.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.TYPE})
public @interface Traced {
    /**
     * enable opentracing
     * @return default true
     */
    boolean value() default true;

    /**
     * log the method inputs
     * @return default false
     */
    boolean logInputs() default false;

    /**
     * log the method output
     * @return default false
     */
    boolean logOutput() default false;
}
