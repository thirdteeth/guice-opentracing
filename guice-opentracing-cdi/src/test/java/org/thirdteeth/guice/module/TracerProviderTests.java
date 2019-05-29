package org.thirdteeth.guice.module;

import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class TracerProviderTests {
    @Test
    public void defaultsToNotSkip() {
        System.clearProperty(TracerProvider.SKIP_PROPERTY);
        assertFalse(new TracerProvider().skip());
    }

    @Test
    public void skipIfSetToTrue() {
        System.clearProperty(TracerProvider.SKIP_PROPERTY);
        System.setProperty(TracerProvider.SKIP_PROPERTY, "true");
        assertTrue(new TracerProvider().skip());

        System.setProperty(TracerProvider.SKIP_PROPERTY, "TrUe");
        assertTrue(new TracerProvider().skip());
    }

    @Test
    public void doNotSkipIfSetToFalse() {
        System.clearProperty(TracerProvider.SKIP_PROPERTY);
        System.setProperty(TracerProvider.SKIP_PROPERTY, "false");
        assertFalse(new TracerProvider().skip());

        System.setProperty(TracerProvider.SKIP_PROPERTY, "FALSE");
        assertFalse(new TracerProvider().skip());

        System.setProperty(TracerProvider.SKIP_PROPERTY, "FalSe");
        assertFalse(new TracerProvider().skip());

        System.setProperty(TracerProvider.SKIP_PROPERTY, "something");
        assertFalse(new TracerProvider().skip());

        System.setProperty(TracerProvider.SKIP_PROPERTY, "");
        assertFalse(new TracerProvider().skip());
    }
}
