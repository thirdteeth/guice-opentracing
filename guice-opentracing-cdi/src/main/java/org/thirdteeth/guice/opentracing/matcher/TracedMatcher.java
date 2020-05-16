package org.thirdteeth.guice.opentracing.matcher;

import com.google.inject.matcher.AbstractMatcher;
import org.thirdteeth.guice.opentracing.Traced;
import org.thirdteeth.guice.opentracing.helper.TracedHelper;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.Optional;

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
        final Optional<Traced> traced = TracedHelper.getTraced(method);
        return traced.map(Traced::value).orElse(false);
    }
}
