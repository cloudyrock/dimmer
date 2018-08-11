package com.github.cloudyrock.dimmer;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

public class FeatureMetadataExceptionTest {

    @Test
    public void equals() {
        final FeatureMetadataException m1 = new FeatureMetadataException("F1", null);
        final FeatureMetadataException m2 = new FeatureMetadataException("F1", null);
        final FeatureMetadataException m3 = new FeatureMetadataException("F2", null);

        assertEquals(m1, m2);
        assertNotEquals(m1, m3);

    }

    @Test
    public void constructor() {
        final Class<? extends RuntimeException> ex = RuntimeException.class;
        final FeatureMetadataException m1 = new FeatureMetadataException("F1", ex);
        assertEquals("F1", m1.getFeature());
        assertEquals(ex, m1.getException());
    }

}