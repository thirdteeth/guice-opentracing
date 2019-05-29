package org.thirdteeth.guice.opentracing;

import org.junit.Assert;
import org.junit.Test;
import org.thirdteeth.guice.matcher.TracedMatcher;

import java.lang.reflect.Method;

public class TracedTests {

    @Test
    public void testTraced() throws NoSuchMethodException {
        Class clazz = TestTracingServiceImpl.class;
        Method method = clazz.getMethod("testMethod", null);
        Assert.assertTrue(new TracedMatcher().matches(method));
        Method method1 = clazz.getMethod("testMethod2", null);
        Assert.assertFalse(new TracedMatcher().matches(method1));
    }

    @Test
    public void testNotTraced() throws NoSuchMethodException {
        Class clazz = TestNoTracingServcieImpl.class;
        Method method = clazz.getMethod("testMethod", null);
        Assert.assertFalse(new TracedMatcher().matches(method));
        Method method1 = clazz.getMethod("testMethod2", null);
        Assert.assertTrue(new TracedMatcher().matches(method1));
        Method method2 = clazz.getMethod("testMethod3", null);
        Assert.assertFalse(new TracedMatcher().matches(method2));
    }
}
