package com.github.cloudyrock.dimmer;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

public class FeatureMetadataUTest {

    private final String operation = "operation";

    @Test
    public void equals() {
        final DummyFeatureMeatadata m1 = new DummyFeatureMeatadata("F1");
        final DummyFeatureMeatadata m2 = new DummyFeatureMeatadata("F1");
        final DummyFeatureMeatadata m3 = new DummyFeatureMeatadata("F2");

        assertEquals(m1, m2);
        assertNotEquals(m1, m3);

    }

    @Test
    public void constructor() {
        final DummyFeatureMeatadata m1 = new DummyFeatureMeatadata("F1");
        assertEquals("F1", m1.getFeature());
    }

    class DummyFeatureMeatadata extends FeatureMetadata{

        protected DummyFeatureMeatadata(String feature) {
            super(feature, operation);
        }
    }

}