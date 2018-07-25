package com.github.cloudyrock.dimmer;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

public class FeatureMetadataValueTest {

    @Test
    public void equals() {
        final FeatureMetadataValue m1 = new FeatureMetadataValue("F1", null);
        final FeatureMetadataValue m2 = new FeatureMetadataValue("F1", null);
        final FeatureMetadataValue m3 = new FeatureMetadataValue("F2", null);

        assertEquals(m1, m2);
        assertNotEquals(m1, m3);

    }

    @Test
    public void constructor() {
        final String value = "VALUE";
        final FeatureMetadataValue m1 = new FeatureMetadataValue("F1", value);
        assertEquals("F1", m1.getFeature());
        assertEquals(value, m1.getValueToReturn());
    }
}