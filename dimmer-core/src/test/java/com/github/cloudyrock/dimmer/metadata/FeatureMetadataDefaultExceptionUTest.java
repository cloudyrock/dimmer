package com.github.cloudyrock.dimmer.metadata;

import com.github.cloudyrock.dimmer.metadata.DefaultExceptionFeatureMetadata;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

public class FeatureMetadataDefaultExceptionUTest {
    private final String operation = "operation";

    @Test
    public void equals() {
        final DefaultExceptionFeatureMetadata m1 =
                new DefaultExceptionFeatureMetadata("F1", operation);
        final DefaultExceptionFeatureMetadata m2 =
                new DefaultExceptionFeatureMetadata("F1", operation);
        final DefaultExceptionFeatureMetadata m3 =
                new DefaultExceptionFeatureMetadata("F2", operation);

        assertEquals(m1, m2);
        assertNotEquals(m1, m3);

    }

    @Test
    public void constructor() {
        final DefaultExceptionFeatureMetadata m1 =
                new DefaultExceptionFeatureMetadata("F1", operation);
        assertEquals("F1", m1.getFeature());
    }

}