package com.github.cloudyrock.dimmer.metadata;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

public class FeatureMetadataUTest {

    private static final String operation = "operation";

    @Test
    public void equals() {
        final DummyFeatureMetadata m1 = new DummyFeatureMetadata("F1");
        final DummyFeatureMetadata m2 = new DummyFeatureMetadata("F1");
        final DummyFeatureMetadata m3 = new DummyFeatureMetadata("F2");

        assertEquals(m1, m2);
        assertNotEquals(m1, m3);

    }

    @Test
    public void constructor() {
        final DummyFeatureMetadata m1 = new DummyFeatureMetadata("F1");
        assertEquals("F1", m1.getFeature());
    }

    class DummyFeatureMetadata extends FeatureMetadata {

        protected DummyFeatureMetadata(String feature) {
            super(feature, FeatureMetadataUTest.operation);
        }
    }

}