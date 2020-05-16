package org.thirdteeth.guice.opentracing.module;

import com.google.inject.AbstractModule;
import com.google.inject.matcher.Matchers;
import io.opentracing.Tracer;
import org.thirdteeth.guice.opentracing.interceptor.OpenTracingInterceptor;
import org.thirdteeth.guice.opentracing.matcher.TracedMatcher;

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
        bindInterceptor(Matchers.any(), new TracedMatcher(),
                new OpenTracingInterceptor());
        bind(Tracer.class).toProvider(TracerProvider.class).asEagerSingleton();
    }
}
