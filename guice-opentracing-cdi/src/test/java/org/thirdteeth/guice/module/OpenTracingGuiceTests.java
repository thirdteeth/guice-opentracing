package org.thirdteeth.guice.module;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Key;
import com.google.inject.name.Names;
import io.opentracing.Scope;
import io.opentracing.Tracer;
import io.opentracing.mock.MockTracer;
import io.opentracing.util.GlobalTracer;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.thirdteeth.guice.opentracing.TestTracingService;

public class OpenTracingGuiceTests {
    private static Injector injector;

    @BeforeClass
    public static void initialGuice() {
        injector = Guice.createInjector(new MockTracerResolverModule());
    }

    @Test
    public void testOpenTracingGuice() {
        TestTracingService service = injector.getInstance(Key.get(TestTracingService.class, Names.named("Traced")));
        MockTracer mockTracer = MockTracerFactory.INSTANCE;
        mockTracer.reset();
        Assert.assertEquals(0, mockTracer.finishedSpans().size());
        service.noTraced();
        Assert.assertEquals(1, mockTracer.finishedSpans().size());
        service.traced();
        Assert.assertEquals(2, mockTracer.finishedSpans().size());
        service.tracedValueFalse();
        Assert.assertEquals(2, mockTracer.finishedSpans().size());
    }

    @Test
    public void testOpenTracingGuiceNoTracedOnClass() {
        TestTracingService service = injector.getInstance(Key.get(TestTracingService.class, Names.named("NoTraced")));
        MockTracer mockTracer = MockTracerFactory.INSTANCE;
        mockTracer.reset();
        Assert.assertEquals(0, mockTracer.finishedSpans().size());
        service.noTraced();
        Assert.assertEquals(0, mockTracer.finishedSpans().size());
        service.traced();
        Assert.assertEquals(1, mockTracer.finishedSpans().size());
        service.tracedValueFalse();
        Assert.assertEquals(1, mockTracer.finishedSpans().size());
    }

    @Test
    public void testOpenTracingGuiceTracer() {
        TestTracingService service = injector.getInstance(Key.get(TestTracingService.class, Names.named("NoTraced")));
        Tracer tracer = injector.getInstance(Tracer.class);
        Tracer.SpanBuilder builder = tracer.buildSpan("test");
        MockTracer mockTracer = MockTracerFactory.INSTANCE;
        mockTracer.reset();
        try (Scope scope = builder.startActive(true)) {
            service.tracedValueFalse();
        }
        Assert.assertEquals(1, mockTracer.finishedSpans().size());
    }

    @Test
    public void testOpenTracingGlobalTracer() {
        TestTracingService service = injector.getInstance(Key.get(TestTracingService.class, Names.named("NoTraced")));
        Tracer tracer = GlobalTracer.get();
        Tracer.SpanBuilder builder = tracer.buildSpan("test");
        MockTracer mockTracer = MockTracerFactory.INSTANCE;
        mockTracer.reset();
        try (Scope scope = builder.startActive(true)) {
            service.tracedValueFalse();
        }
        Assert.assertEquals(1, mockTracer.finishedSpans().size());
    }
}
