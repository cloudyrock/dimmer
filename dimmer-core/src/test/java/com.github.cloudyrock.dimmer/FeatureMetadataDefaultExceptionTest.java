package com.github.cloudyrock.dimmer;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

public class FeatureMetadataDefaultExceptionTest {

    @Test
    public void equals() {
        final FeatureMetadataDefaultException m1 =
                new FeatureMetadataDefaultException("F1");
        final FeatureMetadataDefaultException m2 =
                new FeatureMetadataDefaultException("F1");
        final FeatureMetadataDefaultException m3 =
                new FeatureMetadataDefaultException("F2");

        assertEquals(m1, m2);
        assertNotEquals(m1, m3);

    }

    @Test
    public void constructor() {
        final FeatureMetadataDefaultException m1 =
                new FeatureMetadataDefaultException("F1");
        assertEquals("F1", m1.getFeature());
    }

}