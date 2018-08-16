package com.github.cloudyrock.dimmer;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class BehaviourKeyTest {

    @Test
    public void constructorAndGetters() {
        final BehaviourKey obj = new BehaviourKey("feature", "operation");
        assertEquals("feature", obj.getFeature());
        assertEquals("operation", obj.getOperation());
    }

    @Test
    public void operationShouldBeEmpty_ifNullIsPassed_WhenConstructor() {
        final BehaviourKey obj = new BehaviourKey("feature", null);
        assertEquals("feature", obj.getFeature());
        assertTrue(obj.getOperation().isEmpty());
    }

    @Test(expected = IllegalArgumentException.class)
    public void shoudlThrowException_ifFeatureIsNull_WhenConstructor() {
        final BehaviourKey obj = new BehaviourKey(null, "operation");
    }

}