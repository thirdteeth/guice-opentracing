package org.thirdteeth.guice.opentracing.example;

import com.google.inject.Guice;
import com.google.inject.Injector;
import org.slf4j.bridge.SLF4JBridgeHandler;
import org.thirdteeth.guice.opentracing.module.OpenTracingModule;

public class Example {
    public static void main(String[] args) {
        SLF4JBridgeHandler.removeHandlersForRootLogger();
        SLF4JBridgeHandler.install();
        Injector injector = Guice.createInjector(new OpenTracingModule(), new ExampleModule());
        OrderService orderService = injector.getInstance(OrderService.class);
        orderService.placeOrder();
    }
}
