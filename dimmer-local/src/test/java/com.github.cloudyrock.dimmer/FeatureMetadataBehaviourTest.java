package com.github.cloudyrock.dimmer;

import org.junit.Test;

import java.util.function.Function;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

public class FeatureMetadataBehaviourTest {

    @Test
    public void equals() {
        final FeatureMetadataBehaviour m1 = new FeatureMetadataBehaviour("F1", null);
        final FeatureMetadataBehaviour m2 = new FeatureMetadataBehaviour("F1", null);
        final FeatureMetadataBehaviour m3 = new FeatureMetadataBehaviour("F2", null);

        assertEquals(m1, m2);
        assertNotEquals(m1, m3);

    }

    @Test
    public void constructor() {
        Function<FeatureInvocationBase, ?> behaviour = f-> "Whatever";
        final FeatureMetadataBehaviour m1 = new FeatureMetadataBehaviour("F1", behaviour);
        assertEquals("F1", m1.getFeature());
        assertEquals(behaviour, m1.getBehaviour());
    }

}