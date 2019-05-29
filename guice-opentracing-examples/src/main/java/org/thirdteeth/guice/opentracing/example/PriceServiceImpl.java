package org.thirdteeth.guice.opentracing.example;

import com.google.inject.Inject;
import io.opentracing.Scope;
import io.opentracing.Tracer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PriceServiceImpl implements PriceService {

    @Inject
    private Tracer tracer;

    private Logger logger = LoggerFactory.getLogger(PriceServiceImpl.class);

    @Override
    public void calculatePrice() {
        Tracer.SpanBuilder builder = tracer.buildSpan("calculatePrice");
        try (Scope scope = builder.startActive(true)) {
            logger.info("price calculated");
        }
    }
}
