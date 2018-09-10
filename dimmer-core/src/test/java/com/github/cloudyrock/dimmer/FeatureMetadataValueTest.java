package com.github.cloudyrock.dimmer;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

public class FeatureMetadataValueTest {

    private final String operation = "operation";

    @Test
    public void equals() {
        final FeatureMetadataValue m1 = new FeatureMetadataValue("F1", operation, null);
        final FeatureMetadataValue m2 = new FeatureMetadataValue("F1", operation, null);
        final FeatureMetadataValue m3 = new FeatureMetadataValue("F2", operation, null);

        assertEquals(m1, m2);
        assertNotEquals(m1, m3);

    }

    @Test
    public void constructor() {
        final String value = "VALUE";
        final FeatureMetadataValue m1 = new FeatureMetadataValue("F1", operation, value);
        assertEquals("F1", m1.getFeature());
        assertEquals(value, m1.getValueToReturn());
    }
}