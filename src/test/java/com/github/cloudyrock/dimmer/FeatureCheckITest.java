package com.github.cloudyrock.dimmer;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class FeatureCheckITest extends DimmerTestBase {

    private static final String REAL_VALUE = "real_value";
    private static final String FEATURE1 = "FEATURE1";
    private static final String FEATURE2 = "FEATURE2";
    private static final String FEATURE3 = "FEATURE3";
    private static final String FEATURE4 = "FEATURE4";
    private static final String FEATURE5 = "FEATURE5";

    @Test
    public void featureOffWithBehaviour_return_mock_value() {
        String mock_value3 = "MOCK_VALUE3";
        assertTrue(dimmerConfiguration.featureOffWithBehaviour(
                FEATURE1,
                s -> mock_value3));
        assertEquals(mock_value3, new DummyFeatureCheckClass().feature1Method());
    }

    @Test
    public void featureOffWithValue_return_mock_value() {
        String mock_value1 = "MOCK_VALUE1";
        assertTrue(dimmerConfiguration.featureOffWithValue(FEATURE3, mock_value1));
        assertEquals(mock_value1, new DummyFeatureCheckClass().feature3Method());
    }

    @Test(expected = DimmerInvocationException.class)
    public void featureOffWithException_throws_exception() {
        assertTrue(dimmerConfiguration.featureOffWithDefaultException(FEATURE2));
        new DummyFeatureCheckClass().feature2Method();
    }

    @Test
    public void featureOffWithValue_return_null_when_arg_is_null() {
        assertTrue(dimmerConfiguration.featureOffWithValue(FEATURE5, null));
        assertEquals(null, new DummyFeatureCheckClass().feature5Method());
    }

    @Test
    public void real_method_is_called_when_feature_not_configured() {
        assertEquals(REAL_VALUE, new DummyFeatureCheckClass().feature4Method());
    }

    class DummyFeatureCheckClass {

        @FeatureCheck(feature = FEATURE1)
        String feature1Method() {
            return REAL_VALUE;
        }

        @FeatureCheck(feature = FEATURE2)
        void feature2Method() {
        }

        @FeatureCheck(feature = FEATURE3)
        String feature3Method() {
            return REAL_VALUE;
        }

        @FeatureCheck(feature = FEATURE4)
        String feature4Method() {
            return REAL_VALUE;
        }

        @FeatureCheck(feature = FEATURE5)
        String feature5Method() {
            return REAL_VALUE;
        }
    }

}
