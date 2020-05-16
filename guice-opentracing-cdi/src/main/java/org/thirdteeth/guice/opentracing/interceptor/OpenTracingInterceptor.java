package org.thirdteeth.guice.opentracing.interceptor;

import io.opentracing.Scope;
import io.opentracing.Span;
import io.opentracing.SpanContext;
import io.opentracing.Tracer;
import io.opentracing.log.Fields;
import io.opentracing.tag.Tags;
import io.opentracing.util.GlobalTracer;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.thirdteeth.guice.opentracing.Traced;
import org.thirdteeth.guice.opentracing.helper.TracedHelper;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
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

        final Method method = invocation.getMethod();
        final Optional<Traced> traced = TracedHelper.getTraced(method);
        final boolean logInputs = traced.map(Traced::logInputs).orElse(false);
        final boolean logOutput = traced.map(Traced::logOutput).orElse(false);
        final Tracer tracer = GlobalTracer.get();
        final Tracer.SpanBuilder builder = tracer.buildSpan(invocation.getMethod().getName());
        builder.withTag(Tags.COMPONENT.getKey(), getComponent());
        builder.withTag(getBeanTagName(), invocation.getThis().getClass().getName());
        final int index = getExistingSpanIndex(invocation, builder);

        try (Scope scope = builder.startActive(true)) {

            if (index >= 0) {
                LOG.fine("Overriding the original span context with our new context.");
                invocation.getArguments()[index] = scope.span().context();
            }
            if (logInputs) {
                logMethodInputs(scope.span(), invocation);
            }
            try {
                final Object output = invocation.proceed();
                if (logOutput) {
                    final Map<String, Object> logs = new HashMap<>();
                    logs.put(Fields.MESSAGE, invocation.getMethod().getName() + " output is" + output);
                    scope.span().log(logs);
                }
                return output;
            } catch (Exception ex) {
                final Map<String, Object> logs = new HashMap<>();
                logs.put(Fields.EVENT, "error");
                logs.put(Fields.ERROR_OBJECT, ex);
                logs.put(Fields.MESSAGE, ex.getMessage());
                scope.span().log(logs);
                throw ex;
            }
        }
    }

    private int getExistingSpanIndex(final MethodInvocation invocation, final Tracer.SpanBuilder builder) {
        int index = -1;
        for (int i = 0; i < invocation.getArguments().length; i++) {
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
        return index;
    }

    private void logMethodInputs(final Span span, final MethodInvocation invocation) {

        for (int i = 0; i < invocation.getArguments().length; i++) {
            final Object parameter = invocation.getArguments()[i];
            final Map<String, Object> logs = new HashMap<>();
            if (parameter instanceof SpanContext || parameter instanceof Span) {
                continue;
            }
            logs.put(Fields.MESSAGE, invocation.getMethod().getName() + " input[" + i + "] is " + invocation.getArguments()[i]);
            span.log(logs);
        }
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
