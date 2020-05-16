package org.thirdteeth.guice.opentracing.helper;

import org.thirdteeth.guice.opentracing.Traced;

import java.lang.reflect.Method;
import java.util.Optional;

/**
 * The Traced helper class for helping get the Traced annotation object from method
 */
public final class TracedHelper {

    private TracedHelper() {
        // default constructor
    }

    /**
     * get the Traced annotation object from method if there is
     * @param method get Traced annotation object from this method or class of this method
     * @return Optional Traced
     */
    public static Optional<Traced> getTraced(final Method method) {
        final Class<?> clazz = method.getDeclaringClass();
        Traced trace = null;
        if (clazz.isAnnotationPresent(Traced.class)) {
            trace = clazz.getAnnotation(Traced.class);
        }

        if (method.isAnnotationPresent(Traced.class)) {
            trace = method.getAnnotation(Traced.class);
        }
        return Optional.ofNullable(trace);
    }
}
