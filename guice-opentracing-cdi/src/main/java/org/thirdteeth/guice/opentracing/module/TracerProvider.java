package org.thirdteeth.guice.opentracing.module;

import io.opentracing.Tracer;
import io.opentracing.contrib.tracerresolver.TracerResolver;
import io.opentracing.util.GlobalTracer;

import javax.inject.Provider;
import java.util.logging.Logger;

/**
 *
 */
public class TracerProvider implements Provider<Tracer> {

    static final String SKIP_PROPERTY = "skipCdiTracerInitializer";
    private static final Logger LOG = Logger.getLogger(TracerProvider.class.getName());
    private Boolean skip = true;

    /**
     * the default constructor
     */
    public TracerProvider() {
        //default constructor
    }

    /**
     * Registers a {@link Tracer} with the {@link GlobalTracer}, this provider attempts to locate one via the {@link TracerResolver}, further setting
     * it to the {@link GlobalTracer}, so that a {@link Tracer} can be injected into components from the target application.
     *
     * A tracer is initialized and registered with the {@link GlobalTracer} if there are no tracers registered with it already.
     *
     * This component can be disabled by setting the system property {@code skipCdiTracerInitializer} with any value except
     * "false" (case insensitive).
     * @return {@link Tracer}
     */
    @Override
    public Tracer get() {

        if (skip()) {
            return GlobalTracer.get();
        }

        if (GlobalTracer.isRegistered()) {
            LOG.info("A Tracer is already registered at the GlobalTracer. Skipping resolution.");
            return GlobalTracer.get();
        }

        final Tracer tracer = TracerResolver.resolveTracer();
        if (null == tracer) {
            LOG.info("Could not get a valid OpenTracing Tracer from the classpath. Skipping.");
            return GlobalTracer.get();
        }

        LOG.info(String.format("Registering %s as the OpenTracing Tracer", tracer.getClass().getName()));
        GlobalTracer.register(tracer);
        return GlobalTracer.get();
    }

    /**
     * should skip load tracer form {@link TracerResolver}
     * @return true if have system property -DskipCdiTracerInitializer=true
     */
    boolean skip() {
        if (!this.skip) {
            return skip;
        }

        final String property = System.getProperty(SKIP_PROPERTY);
        if ("true".equalsIgnoreCase(property)) {
            return skip;
        } else {
            skip = false;
        }
        return skip;
    }
}
