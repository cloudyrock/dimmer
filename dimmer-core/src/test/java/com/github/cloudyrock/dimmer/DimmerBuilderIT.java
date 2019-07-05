package com.github.cloudyrock.dimmer;


import com.github.cloudyrock.dimmer.logic.BehaviourBuilder;
import com.github.cloudyrock.dimmer.util.*;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static com.github.cloudyrock.dimmer.util.ConstantsTestUtil.*;

//TODO: add environment tests: configure different environment and ensure the behaviour is from the one executed
//TODO: configuration: when it's in file, but not in builder, vice versa, etc.
//TODO: Should throw DimmerConfigException when real method is void and Configuration of the Feature Invocation has a return type
//TODO: Should get FeatureInvocation as parameter when featureWithCustomException
//TODO: invalid file

public class DimmerBuilderIT {

    @Rule
    public ExpectedException expectedException = ExpectedException.none();
    
    private static final TestFeaturedClass testFeaturedClass = new TestFeaturedClass();
    
    private BehaviourBuilder getBuilderWithBasicConfiguration() {
        return BuilderTestUtil.basicSetUp()
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

}
