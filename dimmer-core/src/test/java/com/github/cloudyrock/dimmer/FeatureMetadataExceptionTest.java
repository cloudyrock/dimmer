package com.github.cloudyrock.dimmer;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

public class FeatureMetadataExceptionTest {

    private final String operation = "operation";
    @Test
    public void equals() {
        final FeatureMetadataException m1 = new FeatureMetadataException("F1", operation, null);
        final FeatureMetadataException m2 = new FeatureMetadataException("F1", operation, null);
        final FeatureMetadataException m3 = new FeatureMetadataException("F2", operation, null);

        assertEquals(m1, m2);
        assertNotEquals(m1, m3);

    }

    @Test
    public void constructor() {
        final Class<? extends RuntimeException> ex = RuntimeException.class;
        final FeatureMetadataException m1 = new FeatureMetadataException("F1", operation, ex);
        assertEquals("F1", m1.getFeature());
        assertEquals(ex, m1.getException());
    }

}