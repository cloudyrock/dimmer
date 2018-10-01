package com.github.cloudyrock.dimmer;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

public class FeatureMetadataExceptionUTest {

    private final String operation = "operation";
    @Test
    public void equals() {
        final ExceptionFeatureMetadata m1 = new ExceptionFeatureMetadata("F1", operation, null);
        final ExceptionFeatureMetadata m2 = new ExceptionFeatureMetadata("F1", operation, null);
        final ExceptionFeatureMetadata m3 = new ExceptionFeatureMetadata("F2", operation, null);

        assertEquals(m1, m2);
        assertNotEquals(m1, m3);

    }

    @Test
    public void constructor() {
        final Class<? extends RuntimeException> ex = RuntimeException.class;
        final ExceptionFeatureMetadata m1 = new ExceptionFeatureMetadata("F1", operation, ex);
        assertEquals("F1", m1.getFeature());
        assertEquals(ex, m1.getException());
    }

}