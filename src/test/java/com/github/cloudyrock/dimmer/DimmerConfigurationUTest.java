package com.github.cloudyrock.dimmer;

import org.junit.Before;
import org.junit.Test;

import java.util.UUID;
import java.util.function.Function;

import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

public class DimmerConfigurationUTest extends DimmerTestBase {

    private String feature;
    final Function<FeatureInvocation, Object> f1 = s -> "return1";
    final Function<FeatureInvocation, Object> f2 = s -> "return2";

    @Before
    public void setUp() {
        feature = "FEATURE" + UUID.randomUUID().toString();
    }

    @Test
    public void getBehaviour_return_added_value() {

        final Function<FeatureInvocation, Object> f1 = s -> "return1";
        dimmerConfiguration.featureOffWithBehaviour(feature, f1);
        assertEquals(f1, dimmerConfiguration.getBehaviour(feature));
    }

    @Test
    public void featureOffWithBehaviour() {

        boolean putFunction1 = dimmerConfiguration.featureOffWithBehaviour(feature, f1);
        boolean putFunction2 = dimmerConfiguration.featureOffWithBehaviour(feature, f2);
        boolean putDefaultException =
                dimmerConfiguration.featureOffWithDefaultException(feature);
        boolean putValue = dimmerConfiguration.featureOffWithValue(feature, "");
        boolean putException = dimmerConfiguration
                .featureOffWithException(feature, DummyException.class);
        assertEquals(f1, dimmerConfiguration.getBehaviour(feature));
        assertTrue(putFunction1);
        assertFalse(putFunction2);
        assertFalse(putDefaultException);
        assertFalse(putValue);
        assertFalse(putException);

    }

    @Test
    public void featureOffWithValue() {
        boolean putValue = dimmerConfiguration.featureOffWithValue(feature, "VALUE");
        boolean putValue2 = dimmerConfiguration.featureOffWithValue(feature, "VALUE");
        boolean putFunction = dimmerConfiguration.featureOffWithBehaviour(feature, f1);
        boolean putDefaultException =
                dimmerConfiguration.featureOffWithDefaultException(feature);
        boolean putException = dimmerConfiguration
                .featureOffWithException(feature, DummyException.class);
        assertEquals("VALUE", dimmerConfiguration.getBehaviour(feature).apply(null));
        assertTrue(putValue);
        assertFalse(putValue2);
        assertFalse(putDefaultException);
        assertFalse(putFunction);
        assertFalse(putException);
    }

    @Test(expected = DimmerInvocationException.class)
    public void featureOffWithDefaultException() {
        boolean putDefaultException =
                dimmerConfiguration.featureOffWithDefaultException(feature);
        boolean putValue = dimmerConfiguration.featureOffWithValue(feature, "VALUE");
        boolean putFunction = dimmerConfiguration.featureOffWithBehaviour(feature, f1);
        boolean putException = dimmerConfiguration
                .featureOffWithException(feature, DummyException.class);
        assertEquals("VALUE", dimmerConfiguration.getBehaviour(feature).apply(null));
        assertTrue(putDefaultException);
        assertFalse(putValue);
        assertFalse(putFunction);
        assertFalse(putException);

        dimmerConfiguration.getBehaviour(feature).apply(null);
    }

    @Test(expected = DummyException.class)
    public void featureOffWithException() {
        boolean putException = dimmerConfiguration
                .featureOffWithException(feature, DummyException.class);
        boolean putDefaultException =
                dimmerConfiguration.featureOffWithDefaultException(feature);
        boolean putValue = dimmerConfiguration.featureOffWithValue(feature, "VALUE");
        boolean putFunction = dimmerConfiguration.featureOffWithBehaviour(feature, f1);
        assertEquals("VALUE", dimmerConfiguration.getBehaviour(feature).apply(null));
        assertTrue(putException);
        assertFalse(putDefaultException);
        assertFalse(putValue);
        assertFalse(putFunction);

        dimmerConfiguration.getBehaviour(feature).apply(null);
    }

    @Test(expected = DimmerConfigException.class)
    public void featureWithException_throws_exception_when_constructor_not_accessible() {
        dimmerConfiguration
                .featureOffWithException(feature, DummyNoConstructorException.class);
        dimmerConfiguration.getBehaviour(feature).apply(null);
    }

}
