package org.thirdteeth.guice.opentracing.module;

import io.opentracing.Tracer;
import io.opentracing.contrib.tracerresolver.TracerFactory;
import io.opentracing.mock.MockTracer;
import io.opentracing.util.ThreadLocalScopeManager;

public class MockTracerFactory implements TracerFactory {
    public static final MockTracer INSTANCE = new MockTracer(new ThreadLocalScopeManager());

    @Override
    public Tracer getTracer() {
        return INSTANCE;
    }
}