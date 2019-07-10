package com.github.cloudyrock.dimmer;


import com.github.cloudyrock.dimmer.logic.BehaviourBuilder;
import com.github.cloudyrock.dimmer.logic.DimmerBuilder;
import com.github.cloudyrock.dimmer.util.*;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static com.github.cloudyrock.dimmer.util.ConstantsTestUtil.*;
import static org.junit.Assert.assertEquals;

//TODO: Should throw DimmerConfigException when real method is void and Configuration of the Feature Invocation has a return type
//TODO: configuration: when it's in file, but not in builder, vice versa, etc.
//TODO: configuration file with empty list of features, shouldn't throw exception, but og a warning

public class DimmerBuilderIT {

    @Rule
    public ExpectedException expectedException = ExpectedException.none();
    
    private static final TestFeaturedClass testFeaturedClass = new TestFeaturedClass();
    
    private BehaviourBuilder getBuilderWithBasicConfiguration() {
        return DimmerBuilder
                .environments(DEV_ENVIRONMENT, DEFAULT_ENVIRONMENT)
                .featureWithBehaviour(FEATURE_FIXED, OPERATION_BEHAVIOUR, f -> BEHAVIOUR_VALUE)
                .featureWithValue(FEATURE_FIXED, OPERATION_VALUE, TOGGLED_OFF_VALUE)
                .featureWithCustomException(FEATURE_FIXED, OPERATION_CUSTOM_EXCEPTION, CustomException.class)
                .featureWithDefaultException(FEATURE_FIXED, OPERATION_DEFAULT_EXCEPTION)
                .withProperties(LOCAL_CONFIG_FILE);
    }

    @Test
    @DisplayName("Should run behaviour when it's fixed behaviour-configured(non conditional)")
    public void shouldRunBehaviourNonConditional() {
        getBuilderWithBasicConfiguration().runWithDefaultEnvironment();
        assertEquals(BEHAVIOUR_VALUE, testFeaturedClass.operationWithBehaviourFixed());
    }

    @Test
    @DisplayName("Should return value when it's fixed value-configured(non conditional)")
    public void shouldReturnValueNonConditional() {
        getBuilderWithBasicConfiguration().runWithDefaultEnvironment();;
        assertEquals(TOGGLED_OFF_VALUE, testFeaturedClass.operationWithValueFixed());
    }


    @Test(expected = CustomException.class)
    @DisplayName("Should throw custom exception when it's custom-exception-fixed configured(non conditional)")
    public void shouldThrowCustomExceptionNonConditional() {
        getBuilderWithBasicConfiguration().runWithDefaultEnvironment();;
        testFeaturedClass.operationWithCustomExceptionFixed();
    }

    @Test(expected = DimmerInvocationException.class)
    @DisplayName("Should throw default exception when it's fixed default-exception-configured(non conditional)")
    public void shouldThrowDefaultExceptionNonConditional() {
        getBuilderWithBasicConfiguration().runWithDefaultEnvironment();
        testFeaturedClass.operationWithDefaultExceptionFixed();
    }



    @Test(expected = NewDefaultExceptionException.class)
    @DisplayName("Should throw default exception when it's fixed default-exception-configured(non conditional)")
    public void shouldThrowDefaultExceptionUpdatedNonConditional() {
        getBuilderWithBasicConfiguration().setDefaultExceptionType(NewDefaultExceptionException.class).runWithDefaultEnvironment();
        testFeaturedClass.operationWithDefaultExceptionFixed();
    }


    @Test(expected = NewDefaultExceptionWithFeatureInvocationException.class)
    @DisplayName("Should throw default exception and pass featureInvocation at construction when it's fixed default-exception-configured(non conditional)")
    public void shouldThrowDefaultExceptionWithFeatureInvocation() {
        getBuilderWithBasicConfiguration().setDefaultExceptionType(NewDefaultExceptionWithFeatureInvocationException.class).runWithDefaultEnvironment();
        try {
            testFeaturedClass.operationWithDefaultExceptionWithFeatureInvocation("argument-1", new ArgumentClass("argument-2"));
        } catch (NewDefaultExceptionWithFeatureInvocationException ex) {
            FeatureInvocation f = ex.getFeatureInvocation();
            assertEquals(FEATURE_FIXED, f.getFeature());
            assertEquals(OPERATION_DEFAULT_EXCEPTION, f.getOperation());
            assertEquals("operationWithDefaultExceptionWithFeatureInvocation", f.getMethodName());
            assertEquals(String.class, f.getReturnType());
            assertEquals(testFeaturedClass.getClass(), f.getDeclaringType());
            assertEquals("argument-1", f.getArgs()[0]);
            assertEquals(new ArgumentClass("argument-2"), f.getArgs()[1]);
            throw ex;
        }
    }

}
