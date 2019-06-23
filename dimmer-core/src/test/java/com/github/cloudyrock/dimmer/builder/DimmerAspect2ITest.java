package com.github.cloudyrock.dimmer.builder;

import com.github.cloudyrock.dimmer.*;
import org.aspectj.lang.Aspects;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static org.junit.Assert.*;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;

public class DimmerAspect2ITest {
    //CONFIG FILES
    private static final String LOCAL_CONFIG_FILE = "dimmer-it-test.yml";

    //ENVIRONMENTS
    private static final String DEV_ENVIRONMENT= "dev";
    private static final String DEFAULT_ENVIRONMENT= "defaultEnvironment";
    
    //FEATURES
    private static final String FEATURE_FIXED = "FEATURE_FIXED";
    private static final String FEATURE_CONDITIONAL_FALSE = "FEATURE_CONDITIONAL_FALSE";
    private static final String FEATURE_CONDITIONAL_TRUE = "FEATURE_CONDITIONAL_TRUE";
    //OPERATIONS
    private static final String OPERATION_1_BEHAVIOUR = "OPERATION_1_BEHAVIOUR";
    private static final String OPERATION_1_VALUE = "OPERATION_1_VALUE";
    private static final String OPERATION_1_CUSTOM_EXCEPTION = "OPERATION_1_CUSTOM_EXCEPTION";
    private static final String OPERATION_1_DEFAULT_EXCEPTION = "OPERATION_1_DEFAULT_EXCEPTION";

    //VALUES
    private static final String BEHAVIOUR_VALUE = "BEHAVIOUR_VALUE";
    private static final String TOGGLED_OFF_VALUE = "TOGGLED_OFF_VALUE";
    private static final String REAL_VALUE = "real_value";

    @Rule public ExpectedException exception = ExpectedException.none();

    private static final TestFeaturedClass testFeaturedClass = new TestFeaturedClass();

    @BeforeClass
    public static void staticSetup() {
        DimmerBuilder
                .environments(DEV_ENVIRONMENT, DEFAULT_ENVIRONMENT)
                .featureWithBehaviour(FEATURE_FIXED, OPERATION_1_BEHAVIOUR, f -> BEHAVIOUR_VALUE)
                .featureWithValue(FEATURE_FIXED, OPERATION_1_VALUE, TOGGLED_OFF_VALUE)
                .featureWithCustomException(FEATURE_FIXED, OPERATION_1_CUSTOM_EXCEPTION, DummyException.class)
                .featureWithDefaultException(FEATURE_FIXED, OPERATION_1_DEFAULT_EXCEPTION)

                //conditional configuration non executing: false
                .featureWithBehaviourConditional(false, FEATURE_CONDITIONAL_FALSE, OPERATION_1_BEHAVIOUR, f -> BEHAVIOUR_VALUE)
                .featureWithValueConditional(false, FEATURE_CONDITIONAL_FALSE, OPERATION_1_VALUE, TOGGLED_OFF_VALUE)
                .featureWithCustomExceptionConditional(false, FEATURE_CONDITIONAL_FALSE, OPERATION_1_CUSTOM_EXCEPTION, DummyException.class)
                .featureWithDefaultExceptionConditional(false, FEATURE_CONDITIONAL_FALSE, OPERATION_1_DEFAULT_EXCEPTION)

                //conditional configuration executing: true
                .featureWithBehaviourConditional(true, FEATURE_CONDITIONAL_TRUE, OPERATION_1_BEHAVIOUR, f -> BEHAVIOUR_VALUE)
                .featureWithValueConditional(true, FEATURE_CONDITIONAL_TRUE, OPERATION_1_VALUE, TOGGLED_OFF_VALUE)
                .featureWithCustomExceptionConditional(true, FEATURE_CONDITIONAL_TRUE, OPERATION_1_CUSTOM_EXCEPTION, DummyException.class)
                .featureWithDefaultExceptionConditional(true, FEATURE_CONDITIONAL_TRUE, OPERATION_1_DEFAULT_EXCEPTION)
                .withProperties(LOCAL_CONFIG_FILE)
                .setDefaultExceptionType(DefaultException.class)
                .runWithDefaultEnvironment();
    }

    @Test
    public void hasAspect() {
        assertTrue(Aspects.hasAspect(DimmerAspect.class));
    }

    @Test
    @DisplayName("Should run behaviour when it's fixed behaviour-configured(non conditional)")
    public void shouldRunBehaviourNonConditional() {
        assertEquals(BEHAVIOUR_VALUE, testFeaturedClass.operationWithBehabiourFixed());
    }

    @Test
    @DisplayName("Should return value when it's fixed value-configured(non conditional)")
    public void shouldReturnValueNonConditional() {
        assertEquals(TOGGLED_OFF_VALUE, testFeaturedClass.operationWithValueFixed());
    }

    @Test(expected = DummyException.class)
    @DisplayName("Should throw custom exception when it's custom-exception-fixed configured(non conditional)")
    public void shouldThrowCustomExceptionNonConditional() {
        testFeaturedClass.operationWithCustomExceptionFixed();
    }

    @Test(expected = DefaultException.class)
    @DisplayName("Should throw default exception when it's fixed default-exception-configured(non conditional)")
    public void shouldThrowDefaultExceptionNonConditional() {
        testFeaturedClass.operationWithDefaultExceptionFixed();
    }

    //CONDITIONAL FALSE
    @Test
    @DisplayName("Should return real value when it's conditional-false behaviour-configured")
    public void shouldReturnRealValueWhenBehaviourIfConditionalFalse() {
        assertEquals(REAL_VALUE, testFeaturedClass.operationWithBehabiourConditionalFalse());
    }

    @Test
    @DisplayName("Should return real value when it's conditional-false value-configured")
    public void shouldReturnRealValueWhenValueIfConditionalFalse() {
        assertEquals(REAL_VALUE, testFeaturedClass.operationWithValueConditionalFalse());
    }

    @DisplayName("Should return real value when it's conditional-false custom-exception-configured")
    public void shouldReturnRealValueWhenCustomExceptionIfConditionalFalse() {
        assertEquals(REAL_VALUE, testFeaturedClass.operationWithCustomExceptionConditionalFalse());
    }

    @DisplayName("Should return real value when it's conditional-false default-exception-configured")
    public void shouldReturnRealValueWhenDefaultExceptionIfConditionalFalse() {
        assertEquals(REAL_VALUE, testFeaturedClass.operationWithDefaultExceptionConditionalFalse());
    }

    //CONDITIONAL TRUE
    @Test
    @DisplayName("Should run behaviour when it's conditional-true behaviour-configured")
    public void shouldRunBehaviourConditionalTrue() {
        assertEquals(BEHAVIOUR_VALUE, testFeaturedClass.operationWithBehabiourConditionalTrue());
    }

    @Test
    @DisplayName("Should return value when it's conditional-true value-configured")
    public void shouldReturnValueConditionalTrue() {
        assertEquals(TOGGLED_OFF_VALUE, testFeaturedClass.operationWithValueConditionalTrue());
    }

    @Test(expected = DummyException.class)
    @DisplayName("Should throw custom exception when it's conditional-true custom-exception-configured")
    public void shouldThrowCustomExceptionConditionalTrue() {
        testFeaturedClass.operationWithCustomExceptionConditionalTrue();
    }

    @Test(expected = DefaultException.class)
    @DisplayName("Should throw default exception when it's conditional-true default-exception-configured")
    public void shouldThrowDefaultExceptionConditionalTrue() {
        testFeaturedClass.operationWithDefaultExceptionConditionalTrue();
    }



    static class TestFeaturedClass {

        //FEATURE_FIXED
        @DimmerFeature(value = FEATURE_FIXED, op = OPERATION_1_BEHAVIOUR)
        String operationWithBehabiourFixed() {
            return REAL_VALUE;
        }

        @DimmerFeature(value = FEATURE_FIXED, op = OPERATION_1_VALUE)
        String operationWithValueFixed() {
            return REAL_VALUE;
        }

        @DimmerFeature(value = FEATURE_FIXED, op = OPERATION_1_CUSTOM_EXCEPTION)
        String operationWithCustomExceptionFixed() {
            return REAL_VALUE;
        }

        @DimmerFeature(value = FEATURE_FIXED, op = OPERATION_1_DEFAULT_EXCEPTION)
        String operationWithDefaultExceptionFixed() {
            return REAL_VALUE;
        }
        
        //FEATURE_CONDITIONAL_FALSE
        @DimmerFeature(value = FEATURE_CONDITIONAL_FALSE, op = OPERATION_1_BEHAVIOUR)
        String operationWithBehabiourConditionalFalse() {
            return REAL_VALUE;
        }

        @DimmerFeature(value = FEATURE_CONDITIONAL_FALSE, op = OPERATION_1_VALUE)
        String operationWithValueConditionalFalse() {
            return REAL_VALUE;
        }

        @DimmerFeature(value = FEATURE_CONDITIONAL_FALSE, op = OPERATION_1_CUSTOM_EXCEPTION)
        String operationWithCustomExceptionConditionalFalse() {
            return REAL_VALUE;
        }

        @DimmerFeature(value = FEATURE_CONDITIONAL_FALSE, op = OPERATION_1_DEFAULT_EXCEPTION)
        String operationWithDefaultExceptionConditionalFalse() {
            return REAL_VALUE;
        }
        
        //FEATURE_CONDITIONAL_FALSE
        @DimmerFeature(value = FEATURE_CONDITIONAL_TRUE, op = OPERATION_1_BEHAVIOUR)
        String operationWithBehabiourConditionalTrue() {
            return REAL_VALUE;
        }

        @DimmerFeature(value = FEATURE_CONDITIONAL_TRUE, op = OPERATION_1_VALUE)
        String operationWithValueConditionalTrue() {
            return REAL_VALUE;
        }

        @DimmerFeature(value = FEATURE_CONDITIONAL_TRUE, op = OPERATION_1_CUSTOM_EXCEPTION)
        String operationWithCustomExceptionConditionalTrue() {
            return REAL_VALUE;
        }

        @DimmerFeature(value = FEATURE_CONDITIONAL_TRUE, op = OPERATION_1_DEFAULT_EXCEPTION)
        String operationWithDefaultExceptionConditionalTrue() {
            return REAL_VALUE;
        }

    }

    public class ReturnedClassParent {
    }

    public class ReturnedClassChild extends ReturnedClassParent {
    }

}
