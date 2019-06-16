package com.github.cloudyrock.dimmer.metadata;

import com.github.cloudyrock.dimmer.metadata.ValueFeatureMetadata;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

public class FeatureMetadataValueUTest {

    private final String operation = "operation";

    @Test
    public void equals() {
        final ValueFeatureMetadata m1 = new ValueFeatureMetadata("F1", operation, null);
        final ValueFeatureMetadata m2 = new ValueFeatureMetadata("F1", operation, null);
        final ValueFeatureMetadata m3 = new ValueFeatureMetadata("F2", operation, null);

        assertEquals(m1, m2);
        assertNotEquals(m1, m3);

    }

    @Test
    public void constructor() {
        final String value = "VALUE";
        final ValueFeatureMetadata m1 = new ValueFeatureMetadata("F1", operation, value);
        assertEquals("F1", m1.getFeature());
        assertEquals(value, m1.getValueToReturn());
    }
}