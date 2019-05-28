package org.thirdteeth.guice.interceptor;

import io.opentracing.Scope;
import io.opentracing.Span;
import io.opentracing.SpanContext;
import io.opentracing.Tracer;
import io.opentracing.tag.Tags;
import io.opentracing.util.GlobalTracer;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.thirdteeth.guice.opentracing.Traced;

import java.lang.reflect.Method;
import java.util.logging.Logger;

/**
 * This is a CDI Interceptor, wrapping the traced methods within an OpenTracing {@link io.opentracing.Span}, should
 * they be annotated with {@link Traced}. The interceptor looks at the type-level and method-level annotations and
 * determines whether the method call should be traced or not.
 *
 * The actual OpenTracing wrapping happens at the more generic {@link OpenTracingInterceptor} and sets the component
 * to "cdi".
 *
 * @see OpenTracingInterceptor
 */
public class OpenTracingInterceptor implements MethodInterceptor {

    private static final Logger LOG = Logger.getLogger(OpenTracingInterceptor.class.getName());

    /**
     * default constructor
     */
    public OpenTracingInterceptor() {
        //default constructor
    }

    /**
     * wrapping the traced methods within an OpenTracing {@link io.opentracing.Span}
     * @param invocation the method invocation that need be traced
     * @return the original method return value
     * @throws Throwable throws exceptions
     */
    @Override
    public Object invoke(final MethodInvocation invocation) throws Throwable {
        if (!GlobalTracer.isRegistered()) {
            LOG.fine("GlobalTracer is not registered. Skipping.");
            return invocation.proceed();
        }

        if (!isTrace(invocation)) {
            return invocation.proceed();
        }

        final Tracer tracer = GlobalTracer.get();
        final Tracer.SpanBuilder builder = tracer.buildSpan(invocation.getMethod().getName());
        builder.withTag(Tags.COMPONENT.getKey(), getComponent());
        builder.withTag(getBeanTagName(), invocation.getThis().getClass().getName());

        int index = -1;
        for (int i = 0; i < invocation.getArguments().length; i += 1) {
            final Object parameter = invocation.getArguments()[i];
            if (parameter instanceof SpanContext) {
                LOG.fine("Found parameter as span context. Using it as the parent of this new span");
                builder.asChildOf((SpanContext) parameter);
                index = i;
                break;
            }

            if (parameter instanceof Span) {
                LOG.fine("Found parameter as span. Using it as the parent of this new span");
                builder.asChildOf((Span) parameter);
                index = i;
                break;
            }
        }

        try (Scope scope = builder.startActive(true)) {

            if (index >= 0) {
                LOG.fine("Overriding the original span context with our new context.");
                invocation.getArguments()[index] = scope.span().context();
            }
            return invocation.proceed();
        }
    }

    private boolean isTrace(final MethodInvocation invocation) {
        final Method method = invocation.getMethod();
        final Class<?> clazz = method.getDeclaringClass();
        boolean trace = true;
        if (clazz.isAnnotationPresent(Traced.class)) {
            trace = clazz.getAnnotation(Traced.class).value();
        }

        if (method.isAnnotationPresent(Traced.class)) {
            trace = method.getAnnotation(Traced.class).value();
        }
        return trace;
    }

    /**
     * return the current component name
     * @return the current component name
     */
    public String getComponent() {
        return "guice";
    }

    /**
     * return the bean tag name in tracing span
     * @return the bean tag name in tracing span
     */
    public String getBeanTagName() {
        return getComponent() + ".bean";
    }
}
