package org.thirdteeth.guice.opentracing;

import java.util.logging.Logger;

@Traced
public class TestTracingServiceImpl implements TestTracingService {
    private static final Logger LOG = Logger.getLogger(TestTracingServiceImpl.class.getName());

    @Override
    public void testMethod() {
        LOG.fine("test message");
    }

    @Traced(value = false)
    public void testMethod2() {
        LOG.fine("test message");
    }
}
