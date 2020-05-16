package org.thirdteeth.guice.opentracing;

import org.junit.Assert;
import org.junit.Test;
import org.thirdteeth.guice.opentracing.matcher.TracedMatcher;

import java.lang.reflect.Method;

public class TracedTests {

    @Test
    public void testTraced() throws NoSuchMethodException {
        Class<TestTracingServiceImpl> clazz = TestTracingServiceImpl.class;
        Method method = clazz.getMethod("noTraced", null);
        Assert.assertTrue(new TracedMatcher().matches(method));
        Method method1 = clazz.getMethod("traced", null);
        Assert.assertTrue(new TracedMatcher().matches(method1));
        Method method2 = clazz.getMethod("tracedValueFalse", null);
        Assert.assertFalse(new TracedMatcher().matches(method2));
    }

    @Test
    public void testNotTraced() throws NoSuchMethodException {
        Class<TestNoTracingServiceImpl> clazz = TestNoTracingServiceImpl.class;
        Method method = clazz.getMethod("noTraced", null);
        Assert.assertFalse(new TracedMatcher().matches(method));
        Method method1 = clazz.getMethod("traced", null);
        Assert.assertTrue(new TracedMatcher().matches(method1));
        Method method2 = clazz.getMethod("tracedValueFalse", null);
        Assert.assertFalse(new TracedMatcher().matches(method2));
    }
}
