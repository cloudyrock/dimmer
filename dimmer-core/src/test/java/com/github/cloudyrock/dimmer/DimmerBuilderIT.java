package com.github.cloudyrock.dimmer;


import com.github.cloudyrock.dimmer.logic.BehaviourBuilder;
import com.github.cloudyrock.dimmer.logic.DimmerBuilder;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static org.junit.Assert.assertEquals;

//TODO: add environment tests: configure different environment and ensure the behaviour is from the one executed
//TODO: checking the right signature is injected
//TODO: throwing an exception inside a behaviour should be propagated
//TODO: configuration: when is in file, but not in builder, vice versa, etc.
//TODO: throw config exception when method's signature and the value to return(withValue) mismatch
//TODO: should return a null value when withValue
//TODO: Should throw DimmerConfigException when real method is void and Configuration of the Feature Invocation has a return type
//TODO: Should get FeatureInvocation as parameter when featureWithCustomException
//TODO: invalid file

public class DimmerBuilderIT {
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
    private static final String OPERATION_RETURNS_CUSTOM_OBJECT = "OPERATION_RETURNS_CUSTOM_OBJECT";

    //VALUES
    private static final String BEHAVIOUR_VALUE = "BEHAVIOUR_VALUE";
    private static final String TOGGLED_OFF_VALUE = "TOGGLED_OFF_VALUE";
    private static final String REAL_VALUE = "real_value";
    private static final String CHILD_VALUE = "CHILD_VALUE";

    @Rule public ExpectedException exception = ExpectedException.none();

    private static final TestFeaturedClass testFeaturedClass = new TestFeaturedClass();

    public static void setUp() {
        setUp(null);
    }

    public static <T extends RuntimeException> void setUp(Class<T> defaultException) {
        BehaviourBuilder builder = DimmerBuilder
                .environments(DEV_ENVIRONMENT, DEFAULT_ENVIRONMENT)
                .featureWithBehaviour(FEATURE_FIXED, OPERATION_1_BEHAVIOUR, f -> BEHAVIOUR_VALUE)
                .featureWithValue(FEATURE_FIXED, OPERATION_1_VALUE, TOGGLED_OFF_VALUE)
                .featureWithCustomException(FEATURE_FIXED, OPERATION_1_CUSTOM_EXCEPTION, CustomException.class)
                .featureWithDefaultException(FEATURE_FIXED, OPERATION_1_DEFAULT_EXCEPTION)
                .featureWithBehaviour(FEATURE_FIXED, OPERATION_RETURNS_CUSTOM_OBJECT, f-> new ReturnedClassChild(CHILD_VALUE))

                //conditional configuration non executing(false)
                .featureWithBehaviourConditional(false, FEATURE_CONDITIONAL_FALSE, OPERATION_1_BEHAVIOUR, f -> BEHAVIOUR_VALUE)
                .featureWithValueConditional(false, FEATURE_CONDITIONAL_FALSE, OPERATION_1_VALUE, TOGGLED_OFF_VALUE)
                .featureWithCustomExceptionConditional(false, FEATURE_CONDITIONAL_FALSE, OPERATION_1_CUSTOM_EXCEPTION, CustomException.class)
                .featureWithDefaultExceptionConditional(false, FEATURE_CONDITIONAL_FALSE, OPERATION_1_DEFAULT_EXCEPTION)

                //conditional configuration executing(true)
                .featureWithBehaviourConditional(true, FEATURE_CONDITIONAL_TRUE, OPERATION_1_BEHAVIOUR, f -> BEHAVIOUR_VALUE)
                .featureWithValueConditional(true, FEATURE_CONDITIONAL_TRUE, OPERATION_1_VALUE, TOGGLED_OFF_VALUE)
                .featureWithCustomExceptionConditional(true, FEATURE_CONDITIONAL_TRUE, OPERATION_1_CUSTOM_EXCEPTION, CustomException.class)
                .featureWithDefaultExceptionConditional(true, FEATURE_CONDITIONAL_TRUE, OPERATION_1_DEFAULT_EXCEPTION)
                .withProperties(LOCAL_CONFIG_FILE);

        if(defaultException != null) {
            builder = builder.setDefaultExceptionType(defaultException);
        }
        builder.runWithDefaultEnvironment();
    }


    @Test
    @DisplayName("Should run behaviour when it's fixed behaviour-configured(non conditional)")
    public void shouldRunBehaviourNonConditional() {
        setUp();
        assertEquals(BEHAVIOUR_VALUE, testFeaturedClass.operationWithBehaviourFixed());
    }

    @Test
    @DisplayName("Should return value when it's fixed value-configured(non conditional)")
    public void shouldReturnValueNonConditional() {
        setUp();
        assertEquals(TOGGLED_OFF_VALUE, testFeaturedClass.operationWithValueFixed());
    }

    @Test(expected = CustomException.class)
    @DisplayName("Should throw custom exception when it's custom-exception-fixed configured(non conditional)")
    public void shouldThrowCustomExceptionNonConditional() {
        setUp();
        testFeaturedClass.operationWithCustomExceptionFixed();
    }

    @Test(expected = DimmerInvocationException.class)
    @DisplayName("Should throw default exception when it's fixed default-exception-configured(non conditional)")
    public void shouldThrowDefaultExceptionNonConditional() {
        setUp();
        testFeaturedClass.operationWithDefaultExceptionFixed();
    }



    @Test(expected = NewDefaultExceptionException.class)
    @DisplayName("Should throw default exception when it's fixed default-exception-configured(non conditional)")
    public void shouldThrowDefaultExceptionUpdatedNonConditional() {
        setUp(NewDefaultExceptionException.class);
        testFeaturedClass.operationWithDefaultExceptionFixed();
    }

    //CONDITIONAL FALSE
    @Test
    @DisplayName("Should return real value when it's conditional-false behaviour-configured")
    public void shouldReturnRealValueWhenBehaviourIfConditionalFalse() {
        setUp();
        assertEquals(REAL_VALUE, testFeaturedClass.operationWithBehaviourConditionalFalse());
    }

    @Test
    @DisplayName("Should return real value when it's conditional-false value-configured")
    public void shouldReturnRealValueWhenValueIfConditionalFalse() {
        setUp();
        assertEquals(REAL_VALUE, testFeaturedClass.operationWithValueConditionalFalse());
    }

    @Test
    @DisplayName("Should return real value when it's conditional-false custom-exception-configured")
    public void shouldReturnRealValueWhenCustomExceptionIfConditionalFalse() {
        setUp();
        assertEquals(REAL_VALUE, testFeaturedClass.operationWithCustomExceptionConditionalFalse());
    }

    @Test
    @DisplayName("Should return real value when it's conditional-false default-exception-configured")
    public void shouldReturnRealValueWhenDefaultExceptionIfConditionalFalse() {
        setUp();
        assertEquals(REAL_VALUE, testFeaturedClass.operationWithDefaultExceptionConditionalFalse());
    }

    //CONDITIONAL TRUE
    @Test
    @DisplayName("Should run behaviour when it's conditional-true behaviour-configured")
    public void shouldRunBehaviourConditionalTrue() {
        setUp();
        assertEquals(BEHAVIOUR_VALUE, testFeaturedClass.operationWithBehaviourConditionalTrue());
    }

    @Test
    @DisplayName("Should return value when it's conditional-true value-configured")
    public void shouldReturnValueConditionalTrue() {
        setUp();
        assertEquals(TOGGLED_OFF_VALUE, testFeaturedClass.operationWithValueConditionalTrue());
    }

    @Test(expected = CustomException.class)
    @DisplayName("Should throw custom exception when it's conditional-true custom-exception-configured")
    public void shouldThrowCustomExceptionConditionalTrue() {
        setUp();
        testFeaturedClass.operationWithCustomExceptionConditionalTrue();
    }

    @Test(expected = DimmerInvocationException.class)
    @DisplayName("Should throw default exception when it's conditional-true default-exception-configured")
    public void shouldThrowDefaultExceptionConditionalTrue() {
        setUp();
        testFeaturedClass.operationWithDefaultExceptionConditionalTrue();
    }

    //GENERAL
    @Test
    @DisplayName("Should run behaviour when it's conditional-true behaviour-configured")
    public void shouldWorkReturningAnInstanceOfAChildClass() {
        setUp();
        ReturnedClassParent parent = testFeaturedClass.operationReturnsCustomObject();
        ReturnedClassChild child = (ReturnedClassChild)parent;
        assertEquals(CHILD_VALUE, child.value);
    }


    static class TestFeaturedClass {

        //FEATURE_FIXED
        @DimmerFeature(value = FEATURE_FIXED, op = OPERATION_1_BEHAVIOUR)
        String operationWithBehaviourFixed() {
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
        String operationWithBehaviourConditionalFalse() {
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
        String operationWithBehaviourConditionalTrue() {
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

        @DimmerFeature(value = FEATURE_FIXED, op = OPERATION_RETURNS_CUSTOM_OBJECT)
        ReturnedClassParent operationReturnsCustomObject() {
            return new ReturnedClassParent();
        }

    }

    public static class ReturnedClassParent {
    }

    public static class ReturnedClassChild extends ReturnedClassParent {
        public String value;

        public ReturnedClassChild(String value) {
            this.value = value;
        }
    }

}
