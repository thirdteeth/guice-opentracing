package org.thirdteeth.guice.opentracing.example;

import com.google.inject.Inject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.thirdteeth.guice.opentracing.Traced;

@Traced
public class OrderServiceImpl implements OrderService {
    private Logger logger = LoggerFactory.getLogger(OrderServiceImpl.class);
    @Inject
    private InventoryService inventoryService;
    @Override
    public void placeOrder() {
        inventoryService.lockInventory();
        logger.info("order placed");
    }
}
