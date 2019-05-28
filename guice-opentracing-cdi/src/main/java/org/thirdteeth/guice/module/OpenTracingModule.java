package org.thirdteeth.guice.module;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.google.inject.matcher.Matchers;
import io.opentracing.Tracer;
import io.opentracing.util.GlobalTracer;
import org.thirdteeth.guice.interceptor.OpenTracingInterceptor;
import org.thirdteeth.guice.opentracing.Traced;

/**
 * The guice module for opentracing {@link Tracer} and bind {@link OpenTracingInterceptor} into guice
 */
public final class OpenTracingModule extends AbstractModule {

    /**
     * default constructor
     */
    public OpenTracingModule() {
        //default constructor
    }

    /**
     * bind {@link OpenTracingInterceptor} into guice
     */
    @Override
    protected void configure() {
        bindInterceptor(Matchers.any(), Matchers.annotatedWith(Traced.class),
                new OpenTracingInterceptor());
    }

    /**
     * provide cdi for opentracing {@link Tracer}
     * @return {@link Tracer}
     */
    @Singleton
    @Provides
    public Tracer tracerProvider() {
        return GlobalTracer.get();
    }
}
