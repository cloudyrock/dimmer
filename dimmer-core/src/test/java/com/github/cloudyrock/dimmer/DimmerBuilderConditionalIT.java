package com.github.cloudyrock.dimmer;


import com.github.cloudyrock.dimmer.logic.BehaviourBuilder;
import com.github.cloudyrock.dimmer.logic.DimmerBuilder;
import com.github.cloudyrock.dimmer.util.*;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static com.github.cloudyrock.dimmer.util.ConstantsTestUtil.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class DimmerBuilderConditionalIT {

    @Rule
    public ExpectedException expectedException = ExpectedException.none();
    
    private static final TestFeaturedClass testFeaturedClass = new TestFeaturedClass();
    
    private BehaviourBuilder getBuilderWithBasicConfiguration() {
        return DimmerBuilder
                .environments(DEV_ENVIRONMENT, DEFAULT_ENVIRONMENT)
                //conditional configuration non executing(false)
                .featureWithBehaviourConditional(false, FEATURE_CONDITIONAL_FALSE, OPERATION_BEHAVIOUR, f -> BEHAVIOUR_VALUE)
                .featureWithValueConditional(false, FEATURE_CONDITIONAL_FALSE, OPERATION_VALUE, TOGGLED_OFF_VALUE)
                .featureWithCustomExceptionConditional(false, FEATURE_CONDITIONAL_FALSE, OPERATION_CUSTOM_EXCEPTION, CustomException.class)
                .featureWithDefaultExceptionConditional(false, FEATURE_CONDITIONAL_FALSE, OPERATION_DEFAULT_EXCEPTION)

                //conditional configuration executing(true)
                .featureWithBehaviourConditional(true, FEATURE_CONDITIONAL_TRUE, OPERATION_BEHAVIOUR, f -> BEHAVIOUR_VALUE)
                .featureWithValueConditional(true, FEATURE_CONDITIONAL_TRUE, OPERATION_VALUE, TOGGLED_OFF_VALUE)
                .featureWithCustomExceptionConditional(true, FEATURE_CONDITIONAL_TRUE, OPERATION_CUSTOM_EXCEPTION, CustomException.class)
                .featureWithDefaultExceptionConditional(true, FEATURE_CONDITIONAL_TRUE, OPERATION_DEFAULT_EXCEPTION)
                .withProperties(LOCAL_CONFIG_FILE);
    }


    //CONDITIONAL FALSE
    @Test
    @DisplayName("Should return real value when it's conditional-false behaviour-configured")
    public void shouldReturnRealValueWhenBehaviourIfConditionalFalse() {
        getBuilderWithBasicConfiguration().runWithDefaultEnvironment();;
        assertEquals(REAL_VALUE, testFeaturedClass.operationWithBehaviourConditionalFalse());
    }

    @Test
    @DisplayName("Should return real value when it's conditional-false value-configured")
    public void shouldReturnRealValueWhenValueIfConditionalFalse() {
        getBuilderWithBasicConfiguration().runWithDefaultEnvironment();;
        assertEquals(REAL_VALUE, testFeaturedClass.operationWithValueConditionalFalse());
    }

    @Test
    @DisplayName("Should return real value when it's conditional-false custom-exception-configured")
    public void shouldReturnRealValueWhenCustomExceptionIfConditionalFalse() {
        getBuilderWithBasicConfiguration().runWithDefaultEnvironment();;
        assertEquals(REAL_VALUE, testFeaturedClass.operationWithCustomExceptionConditionalFalse());
    }

    @Test
    @DisplayName("Should return real value when it's conditional-false default-exception-configured")
    public void shouldReturnRealValueWhenDefaultExceptionIfConditionalFalse() {
        getBuilderWithBasicConfiguration().runWithDefaultEnvironment();;
        assertEquals(REAL_VALUE, testFeaturedClass.operationWithDefaultExceptionConditionalFalse());
    }

    //CONDITIONAL TRUE
    @Test
    @DisplayName("Should run behaviour when it's conditional-true behaviour-configured")
    public void shouldRunBehaviourConditionalTrue() {
        getBuilderWithBasicConfiguration().runWithDefaultEnvironment();;
        assertEquals(BEHAVIOUR_VALUE, testFeaturedClass.operationWithBehaviourConditionalTrue());
    }

    @Test
    @DisplayName("Should return value when it's conditional-true value-configured")
    public void shouldReturnValueConditionalTrue() {
        getBuilderWithBasicConfiguration().runWithDefaultEnvironment();;
        assertEquals(TOGGLED_OFF_VALUE, testFeaturedClass.operationWithValueConditionalTrue());
    }

    @Test(expected = CustomException.class)
    @DisplayName("Should throw custom exception when it's conditional-true custom-exception-configured")
    public void shouldThrowCustomExceptionConditionalTrue() {
        getBuilderWithBasicConfiguration().runWithDefaultEnvironment();;
        testFeaturedClass.operationWithCustomExceptionConditionalTrue();
    }

    @Test(expected = DimmerInvocationException.class)
    @DisplayName("Should throw default exception when it's conditional-true default-exception-configured")
    public void shouldThrowDefaultExceptionConditionalTrue() {
        getBuilderWithBasicConfiguration().runWithDefaultEnvironment();;
        testFeaturedClass.operationWithDefaultExceptionConditionalTrue();
    }

}
