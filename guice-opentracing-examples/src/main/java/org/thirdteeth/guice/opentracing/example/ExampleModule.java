package org.thirdteeth.guice.opentracing.example;

import com.google.inject.AbstractModule;

public class ExampleModule extends AbstractModule {
    @Override
    protected void configure() {
        bind(OrderService.class).to(OrderServiceImpl.class);
        bind(InventoryService.class).to(InventoryServiceImpl.class);
    }
}
