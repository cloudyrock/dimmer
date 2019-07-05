package com.github.cloudyrock.dimmer;


import com.github.cloudyrock.dimmer.logic.BehaviourBuilder;
import com.github.cloudyrock.dimmer.util.*;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static com.github.cloudyrock.dimmer.util.ConstantsTestUtil.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class DimmerBuilderBehaviourIT {

    @Rule
    public ExpectedException expectedException = ExpectedException.none();
    
    private static final TestFeaturedClass testFeaturedClass = new TestFeaturedClass();

    private BehaviourBuilder getBuilderWithBasicConfiguration() {
        return BuilderTestUtil.basicSetUp()
                .withProperties(LOCAL_CONFIG_FILE);
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
        getBuilderWithBasicConfiguration().runWithDefaultEnvironment();;
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
}
