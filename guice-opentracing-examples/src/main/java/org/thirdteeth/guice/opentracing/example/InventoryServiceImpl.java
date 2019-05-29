package org.thirdteeth.guice.opentracing.example;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.thirdteeth.guice.opentracing.Traced;

@Traced
public class InventoryServiceImpl implements InventoryService {
    private Logger logger = LoggerFactory.getLogger(InventoryServiceImpl.class);
    @Override
    public void lockInventory() {
        logger.info("inventory locked");
    }
}
