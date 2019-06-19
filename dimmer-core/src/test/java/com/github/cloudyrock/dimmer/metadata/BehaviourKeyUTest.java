package com.github.cloudyrock.dimmer.metadata;

import com.github.cloudyrock.dimmer.metadata.BehaviourKey;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class BehaviourKeyUTest {

    @Test
    public void constructorAndGetters() {
        final BehaviourKey obj = new BehaviourKey("feature", "operation");
        assertEquals("feature", obj.getFeature());
        assertEquals("operation", obj.getOperation());
    }

    @Test(expected = IllegalArgumentException.class)
    public void operationShouldBeEmpty_ifNullIsPassed_WhenConstructor() {
        final BehaviourKey obj = new BehaviourKey("feature", null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowException_ifFeatureIsNull_WhenConstructor() {
        final BehaviourKey obj = new BehaviourKey(null, "operation");
    }

}