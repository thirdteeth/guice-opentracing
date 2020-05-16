package org.thirdteeth.guice.opentracing.example;

public interface InventoryService {
    String lockInventory(long quantity);
}
