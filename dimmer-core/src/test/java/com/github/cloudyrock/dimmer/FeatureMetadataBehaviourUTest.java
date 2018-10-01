package com.github.cloudyrock.dimmer;

import org.junit.Test;

import java.util.function.Function;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

public class FeatureMetadataBehaviourUTest {

    private final String operation = "operation";

    @Test
    public void equals() {
        final BehaviourFeatureMetadata m1 = new BehaviourFeatureMetadata("F1", operation, null);
        final BehaviourFeatureMetadata m2 = new BehaviourFeatureMetadata("F1", operation, null);
        final BehaviourFeatureMetadata m3 = new BehaviourFeatureMetadata("F2", operation,null);

        assertEquals(m1, m2);
        assertNotEquals(m1, m3);

    }

    @Test
    public void constructor() {
        Function<FeatureInvocation, Object> behaviour = FeatureInvocation::getMethodName;
        final BehaviourFeatureMetadata m1 = new BehaviourFeatureMetadata("F1", operation, behaviour);
        assertEquals("F1", m1.getFeature());
        assertEquals(behaviour, m1.getBehaviour());
    }

}