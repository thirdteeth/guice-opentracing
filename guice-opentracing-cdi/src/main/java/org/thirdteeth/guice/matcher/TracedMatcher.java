package org.thirdteeth.guice.matcher;

import com.google.inject.matcher.AbstractMatcher;
import org.thirdteeth.guice.opentracing.Traced;

import java.io.Serializable;
import java.lang.reflect.Method;

/**
 * a guice method matcher for {@link Traced}, it match method and class that annotated with {@link Traced} and value is true
 */
public class TracedMatcher extends AbstractMatcher<Method> implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * default constructor
     */
    public TracedMatcher() {
        //default constructor
    }

    /**
     * matches method of guice Matcher
     * @see com.google.inject.matcher.Matcher#matches
     * @param method the match method
     * @return boolean
     */
    @Override
    public boolean matches(final Method method) {
        final Class<?> clazz = method.getDeclaringClass();
        boolean trace = false;
        if (clazz.isAnnotationPresent(Traced.class)) {
            trace = clazz.getAnnotation(Traced.class).value();
        }

        if (method.isAnnotationPresent(Traced.class)) {
            trace = method.getAnnotation(Traced.class).value();
        }
        return trace;
    }
}
