package com.github.cloudyrock.dimmer;


import com.github.cloudyrock.dimmer.logic.BehaviourBuilder;
import com.github.cloudyrock.dimmer.logic.DimmerBuilder;
import com.github.cloudyrock.dimmer.util.ArgumentClass;
import com.github.cloudyrock.dimmer.util.DummyRuntimeException;
import com.github.cloudyrock.dimmer.util.TestFeaturedClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static com.github.cloudyrock.dimmer.util.ConstantsTestUtil.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class DimmerBuilderBehaviourIT {

    @Rule
    public ExpectedException expectedException = ExpectedException.none();
    
    private static final TestFeaturedClassBehaviours testFeaturedClass = new TestFeaturedClassBehaviours();
    private BehaviourBuilder getBuilderWithBasicConfiguration() {
        return DimmerBuilder
                .environments(DEV_ENVIRONMENT, DEFAULT_ENVIRONMENT)
                .featureWithValue(FEATURE_FIXED, OPERATION_VALUE_NULL, null)
                .featureWithValue(FEATURE_FIXED, OPERATION_VALUE_MISMATCHING, 1L)
                .featureWithBehaviour(FEATURE_FIXED, OPERATION_BEHAVIOUR_THROWING_EXCEPTION, f -> {throw new DummyRuntimeException();})
                .featureWithBehaviour(FEATURE_FIXED, OPERATION_RETURNS_CUSTOM_OBJECT, f-> new TestFeaturedClass.ReturnedClassChild(CHILD_VALUE))
                .featureWithBehaviour(FEATURE_FIXED, OPERATION_BEHAVIOUR_CHECKING_FEATURE_INVOCATION, f -> {
                    assertEquals(FEATURE_FIXED, f.getFeature());
                    assertEquals(OPERATION_BEHAVIOUR_CHECKING_FEATURE_INVOCATION, f.getOperation());
                    assertEquals("operationWithBehaviourCheckingInvocation", f.getMethodName());
                    assertEquals(String.class, f.getReturnType());
                    assertEquals(TestFeaturedClassBehaviours.class, f.getDeclaringType());
                    assertEquals("value-1", f.getArgs()[0]);
                    assertEquals(new ArgumentClass("value1"), f.getArgs()[1]);
                    return BEHAVIOUR_VALUE;
                })
                .withProperties(CONFIG_FILE_FOR_BEHAVIOURS_TEST);
    }

    @Test
    @DisplayName("Should return child value when behaviour returns child and methods's signature indicates parent ")
    public void shouldReturnChildValueOfInstanceOfParent() {
        getBuilderWithBasicConfiguration().runWithDefaultEnvironment();;
        TestFeaturedClass.ReturnedClassParent parent = testFeaturedClass.operationReturnsCustomObject();
        TestFeaturedClass.ReturnedClassChild child = (TestFeaturedClass.ReturnedClassChild) parent;
        assertEquals(CHILD_VALUE, child.getValue());
    }

    @Test
    @DisplayName("Should inject the right FeatureInvocation to the behaviour")
    public void shouldInjectTheRightFeatureInvocation() {
        getBuilderWithBasicConfiguration().runWithDefaultEnvironment();
        assertEquals(BEHAVIOUR_VALUE, testFeaturedClass.operationWithBehaviourCheckingInvocation("value-1", new ArgumentClass("value1")));
    }

    @Test
    @DisplayName("Should throw exception inside behaviour")
    public void shouldThrowExceptionInsideBehaviour() {
        getBuilderWithBasicConfiguration().runWithDefaultEnvironment();;
        expectedException.expect(DummyRuntimeException.class);
        expectedException.expectMessage(DummyRuntimeException.MESSAGE);
        assertEquals(BEHAVIOUR_VALUE, testFeaturedClass.operationWithBehaviourThrowingExceptionInside());
    }

    @Test
    @DisplayName("Should return null when it's configured to return null as value")
    public void shouldReturnNullValue() {
        getBuilderWithBasicConfiguration().runWithDefaultEnvironment();;
        assertNull(testFeaturedClass.operationWithNullValue());
    }

    @Test
    @DisplayName("Should throw exception qhen mistmached type between real method and feature configuration")
    public void shouldThrowExceptionWhenMismatchingReturnType() {
        getBuilderWithBasicConfiguration().runWithDefaultEnvironment();;
        expectedException.expect(DimmerConfigException.class);
        expectedException.expectMessage("Mismatched returned " +
                "type for method[TestFeaturedClassBehaviours.operationWithMismatchedReturnValue()] with feature[FEATURE_FIXED] " +
                "and operation[OPERATION_VALUE_MISMATCHING]: expected[String], actual returned in behaviour[Long]");
        assertNull(testFeaturedClass.operationWithMismatchedReturnValue());
    }


    @Test
    @DisplayName("Should throw exception qhen mistmached type between real method returning void and feature configuration")
    public void shouldThrowExceptionWhenMismatchingReturnTypeBecauseOfVoid() {
        getBuilderWithBasicConfiguration().runWithDefaultEnvironment();
        expectedException.expect(DimmerConfigException.class);
        expectedException.expectMessage("Mismatched returned " +
                "type for method[TestFeaturedClassBehaviours.operationReturningVoid()] with feature[FEATURE_FIXED] " +
                "and operation[OPERATION_VALUE_MISMATCHING]: expected[void], actual returned in behaviour[Long]");
        testFeaturedClass.operationReturningVoid();
    }

    static class TestFeaturedClassBehaviours {

        @DimmerFeature(value = FEATURE_FIXED, op = OPERATION_RETURNS_CUSTOM_OBJECT)
        TestFeaturedClass.ReturnedClassParent operationReturnsCustomObject() {
            return new TestFeaturedClass.ReturnedClassParent();
        }


        @DimmerFeature(value = FEATURE_FIXED, op = OPERATION_BEHAVIOUR_THROWING_EXCEPTION)
        String operationWithBehaviourThrowingExceptionInside() {
            return REAL_VALUE;
        }

        @DimmerFeature(value = FEATURE_FIXED, op = OPERATION_BEHAVIOUR_CHECKING_FEATURE_INVOCATION)
        String operationWithBehaviourCheckingInvocation(String arg1, ArgumentClass arg2) {
            return REAL_VALUE;
        }


        @DimmerFeature(value = FEATURE_FIXED, op = OPERATION_VALUE_NULL)
        String operationWithNullValue() {
            return REAL_VALUE;
        }

        @DimmerFeature(value = FEATURE_FIXED, op = OPERATION_VALUE_MISMATCHING)
        String operationWithMismatchedReturnValue() {
            return REAL_VALUE;
        }

        @DimmerFeature(value = FEATURE_FIXED, op = OPERATION_VALUE_MISMATCHING)
        void operationReturningVoid() {
        }

    }
}
