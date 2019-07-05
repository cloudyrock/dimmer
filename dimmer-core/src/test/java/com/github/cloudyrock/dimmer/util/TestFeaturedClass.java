package com.github.cloudyrock.dimmer.util;

import com.github.cloudyrock.dimmer.DimmerBuilderIT;
import com.github.cloudyrock.dimmer.DimmerFeature;

import static com.github.cloudyrock.dimmer.util.ConstantsTestUtil.*;
import static com.github.cloudyrock.dimmer.util.ConstantsTestUtil.OPERATION_RETURNS_CUSTOM_OBJECT;

public class TestFeaturedClass {

    //FEATURE_FIXED
    @DimmerFeature(value = FEATURE_FIXED, op = OPERATION_BEHAVIOUR)
    public String operationWithBehaviourFixed() {
        return REAL_VALUE;
    }

    @DimmerFeature(value = FEATURE_FIXED, op = OPERATION_VALUE)
    public String operationWithValueFixed() {
        return REAL_VALUE;
    }






    @DimmerFeature(value = FEATURE_FIXED, op = OPERATION_CUSTOM_EXCEPTION)
    public String operationWithCustomExceptionFixed() {
        return REAL_VALUE;
    }

    @DimmerFeature(value = FEATURE_FIXED, op = OPERATION_DEFAULT_EXCEPTION)
    public String operationWithDefaultExceptionFixed() {
        return REAL_VALUE;
    }

    //FEATURE_CONDITIONAL_FALSE
    @DimmerFeature(value = FEATURE_CONDITIONAL_FALSE, op = OPERATION_BEHAVIOUR)
    public String operationWithBehaviourConditionalFalse() {
        return REAL_VALUE;
    }

    @DimmerFeature(value = FEATURE_CONDITIONAL_FALSE, op = OPERATION_VALUE)
    public String operationWithValueConditionalFalse() {
        return REAL_VALUE;
    }

    @DimmerFeature(value = FEATURE_CONDITIONAL_FALSE, op = OPERATION_CUSTOM_EXCEPTION)
    public String operationWithCustomExceptionConditionalFalse() {
        return REAL_VALUE;
    }

    @DimmerFeature(value = FEATURE_CONDITIONAL_FALSE, op = OPERATION_DEFAULT_EXCEPTION)
    public String operationWithDefaultExceptionConditionalFalse() {
        return REAL_VALUE;
    }

    //FEATURE_CONDITIONAL_FALSE
    @DimmerFeature(value = FEATURE_CONDITIONAL_TRUE, op = OPERATION_BEHAVIOUR)
    public String operationWithBehaviourConditionalTrue() {
        return REAL_VALUE;
    }

    @DimmerFeature(value = FEATURE_CONDITIONAL_TRUE, op = OPERATION_VALUE)
    public String operationWithValueConditionalTrue() {
        return REAL_VALUE;
    }

    @DimmerFeature(value = FEATURE_CONDITIONAL_TRUE, op = OPERATION_CUSTOM_EXCEPTION)
    public String operationWithCustomExceptionConditionalTrue() {
        return REAL_VALUE;
    }

    @DimmerFeature(value = FEATURE_CONDITIONAL_TRUE, op = OPERATION_DEFAULT_EXCEPTION)
    public String operationWithDefaultExceptionConditionalTrue() {
        return REAL_VALUE;
    }




    public static class ReturnedClassParent {
    }

    public static class ReturnedClassChild extends ReturnedClassParent {
        String value;

        public ReturnedClassChild(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }
    }
}


