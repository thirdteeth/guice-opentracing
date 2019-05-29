package org.thirdteeth.guice.opentracing;

import java.util.logging.Logger;

public class TestNoTracingServiceImpl implements TestTracingService {
    private static final Logger LOG = Logger.getLogger(TestTracingServiceImpl.class.getName());

    @Override
    public void noTraced() {
        LOG.fine("test message");
    }

    @Traced
    public void traced() {
        LOG.fine("test message");
    }

    @Traced(value = false)
    public void tracedValueFalse() {
        LOG.fine("test message");
    }
}
