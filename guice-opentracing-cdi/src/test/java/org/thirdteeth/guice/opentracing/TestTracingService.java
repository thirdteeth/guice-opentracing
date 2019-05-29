package org.thirdteeth.guice.opentracing;

public interface TestTracingService {
    void noTraced();
    void traced();
    void tracedValueFalse();
}
