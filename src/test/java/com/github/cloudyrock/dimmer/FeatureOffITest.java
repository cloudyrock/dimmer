package com.github.cloudyrock.dimmer;

import org.junit.Test;

import static com.github.cloudyrock.dimmer.FeatureOffBehaviour.RETURN_NULL;
import static org.junit.Assert.assertNull;

public class FeatureOffITest {

    @Test(expected = DimmerInvocationException.class)
    public void featureOff_throw_default_ex_when_no_params() {
        new DummyFeatureOffClass().throwDefaultException();
    }

    @Test(expected = DummyException.class)
    public void featureOff_throw_custom_exception_when_ex_param() {
        new DummyFeatureOffClass().throwCustomException();
    }

    @Test()
    public void featureOff_return_null_when_RETURN_NULL() {
        assertNull(new DummyFeatureOffClass().returnNull());
    }

    class DummyFeatureOffClass {

        @FeatureOff
        String throwDefaultException() {
            return "REAL CALL";
        }

        @FeatureOff(exception = DummyException.class)
        String throwCustomException() {
            return "REAL CALL";
        }

        @FeatureOff(RETURN_NULL)
        String returnNull() {
            return "REAL CALL";
        }

    }
}
