package org.thirdteeth.guice.module;

import com.google.inject.AbstractModule;
import com.google.inject.name.Names;
import org.thirdteeth.guice.opentracing.TestNoTracingServiceImpl;
import org.thirdteeth.guice.opentracing.TestTracingService;
import org.thirdteeth.guice.opentracing.TestTracingServiceImpl;

public class MockTracerResolverModule extends AbstractModule {
    @Override
    protected void configure() {
        install(new OpenTracingModule());
        bind(TestTracingService.class).annotatedWith(Names.named("Traced")).to(TestTracingServiceImpl.class);
        bind(TestTracingService.class).annotatedWith(Names.named("NoTraced")).to(TestNoTracingServiceImpl.class);
    }
}
