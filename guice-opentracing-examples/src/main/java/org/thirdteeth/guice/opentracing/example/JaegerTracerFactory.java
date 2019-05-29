package org.thirdteeth.guice.opentracing.example;

import io.jaegertracing.internal.JaegerTracer;
import io.opentracing.Tracer;
import io.opentracing.contrib.tracerresolver.TracerFactory;

public class JaegerTracerFactory implements TracerFactory {
    private static final Tracer tracer = new JaegerTracer.Builder("GuiceOpenTracingExample").build();
    @Override
    public Tracer getTracer() {
        return tracer;
    }
}
