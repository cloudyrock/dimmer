package com.github.cloudyrock.dimmer;

import com.github.cloudyrock.dimmer.exceptions.DefaultException;
import com.github.cloudyrock.dimmer.exceptions.DummyException;
import org.aspectj.lang.ProceedingJoinPoint;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.Mock;

import java.util.UUID;
import java.util.function.Function;

import static com.github.cloudyrock.dimmer.DimmerFeature.ALWAYS_OFF;
import static com.github.cloudyrock.dimmer.DimmerFeature.DimmerBehaviour.DEFAULT;
import static com.github.cloudyrock.dimmer.DimmerFeature.DimmerBehaviour.RETURN_NULL;
import static com.github.cloudyrock.dimmer.DimmerFeature.DimmerBehaviour.THROW_EXCEPTION;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.MockitoAnnotations.initMocks;

public class DimmerProcessorUTest extends DimmerTestBase {

    @Rule public final ExpectedException exception = ExpectedException.none();

    @Mock
    private DimmerFeature dimmerFeature;

    @Mock
    private ProceedingJoinPoint jointPoint;

    @Mock
    private Function<FeatureInvocation, String> behaviour;

    private String feature;

    @Before
    public void setUp() {
        feature = "FEATURE" + UUID.randomUUID().toString();
        initMocks(this);
    }

    private void givenDimmerFeatureWithEx(String value,
                                          DimmerFeature.DimmerBehaviour behaviour,
                                          boolean disabled,
                                          Class<? extends RuntimeException> ex) {
        given(dimmerFeature.value()).willReturn(value);
        given(dimmerFeature.behaviour()).willReturn(behaviour);
        given(dimmerFeature.runRealMethod()).willReturn(disabled);
        given(dimmerFeature.exception()).willReturn((Class) ex);
    }

    private void givenDimmerFeature(String value, DimmerFeature.DimmerBehaviour behaviour, boolean disabled) {
        givenDimmerFeatureWithEx(value, behaviour, disabled,
                DimmerFeature.NULL_EXCEPTION.class);
    }

    @Test
    public void when_OFF_NULL_and_enabled_should_return_NULL() throws Throwable {
        givenDimmerFeature(ALWAYS_OFF, RETURN_NULL, false);

        Object returnedValue = dimmerProcessor.executeDimmerFeature(
                dimmerFeature, null, null);

        assertNull(returnedValue);
    }

    @Test(expected = DefaultException.class)
    public void when_OFF_THROW_EX_and_enabled_should_default_THROW_EX() throws Throwable {
        givenDimmerFeature(ALWAYS_OFF, THROW_EXCEPTION, false);
        dimmerProcessor.executeDimmerFeature(
                dimmerFeature, null, null);
    }

    @Test(expected = DummyException.class)
    public void when_OFF_THROW_EX_and_custom_ex_and_enabled_should_custom_THROW_EX()
            throws Throwable {
        givenDimmerFeatureWithEx(ALWAYS_OFF, THROW_EXCEPTION, false,
                DummyException.class);
        dimmerProcessor.executeDimmerFeature(
                dimmerFeature, null, null);
    }

    @Test(expected = DefaultException.class)
    public void when_OFF_DEFAULT_and_enabled_should_THROW_EX() throws Throwable {
        givenDimmerFeature(ALWAYS_OFF, DEFAULT, false);
        dimmerProcessor.executeDimmerFeature(
                dimmerFeature, null, null);
    }

    @Test
    public void when_OFF_and_disabled_should_call_real_method() throws Throwable {
        givenDimmerFeature(ALWAYS_OFF, DEFAULT, true);
        given(jointPoint.proceed()).willReturn("REAL METHOD");

        Object actualValue = dimmerProcessor.executeDimmerFeature(
                dimmerFeature, null, jointPoint);

        then(jointPoint).should().proceed();
        assertEquals(actualValue, "REAL METHOD");
    }

    @Test
    public void when_FEATURE_not_registered_should_call_real_method() throws Throwable {
        givenDimmerFeature(feature, DEFAULT, false);
        given(jointPoint.proceed()).willReturn("REAL METHOD");

        Object actualValue = dimmerProcessor.executeDimmerFeature(
                dimmerFeature, null, jointPoint);

        then(jointPoint).should().proceed();
        assertEquals(actualValue, "REAL METHOD");
    }

    @Test
    public void when_FEATURE_NULL_and_enabled_should_return_NULL() throws Throwable {
        dimmerProcessor.featureWithValue(feature, "VALUE");
        givenDimmerFeature(feature, RETURN_NULL, false);

        Object returnedValue = dimmerProcessor.executeDimmerFeature(
                dimmerFeature, null, null);

        assertNull(returnedValue);
    }

    @Test(expected = DefaultException.class)
    public void when_FEATURE_THROW_EX_and_enabled_should_THROW_EX() throws Throwable {
        dimmerProcessor.featureWithValue(feature, "VALUE");
        givenDimmerFeature(feature, THROW_EXCEPTION, false);

        dimmerProcessor.executeDimmerFeature(
                dimmerFeature, null, null);
    }

    @Test
    public void when_FEATURE_and_disabled_should_call_real_method() throws Throwable {
        dimmerProcessor.featureWithValue(feature, "VALUE");
        givenDimmerFeature(feature, DEFAULT, true);
        given(jointPoint.proceed()).willReturn("REAL METHOD");

        Object actualValue = dimmerProcessor.executeDimmerFeature(
                dimmerFeature, null, jointPoint);

        then(jointPoint).should().proceed();
        assertEquals(actualValue, "REAL METHOD");
    }

    @Test
    public void when_FEATURE_DEFAULT_and_enabled_should_executed_registered_behaviour() throws Throwable {
        dimmerProcessor.featureWithValue(feature, "VALUE");
        givenDimmerFeature(feature, DEFAULT, false);
        Object actualResult = dimmerProcessor.executeDimmerFeature(
                dimmerFeature, null, null);
        assertEquals("VALUE", actualResult);
    }

    @Test
    public void when_featureWithValue_and_enabled_should_executed_return_value() throws Throwable {
        dimmerProcessor.featureWithValue(feature, "VALUE");
        givenDimmerFeature(feature, DEFAULT, false);
        Object actualResult = dimmerProcessor.executeDimmerFeature(
                dimmerFeature, null, null);
        assertEquals("VALUE", actualResult);
    }

    @Test(expected = DefaultException.class)
    public void when_featureWithDefaultException_and_enabled_should_throw_default_exception() throws Throwable {
        dimmerProcessor.featureWithDefaultException(feature);
        givenDimmerFeature(feature, DEFAULT, false);
        dimmerProcessor.executeDimmerFeature(
                dimmerFeature, null, null);
    }

    @Test
    public void when_featureWithBehaviour_and_enabled_should_execute_behaviour_with_featureInvocation() throws Throwable {

        FeatureInvocation featureInvocationMock = mock(FeatureInvocation.class);
        given(behaviour.apply(any(FeatureInvocation.class))).willReturn("BEHAVIOUR");
        givenDimmerFeature(feature, DEFAULT, false);

        dimmerProcessor.featureWithBehaviour(feature, behaviour);
        dimmerProcessor.executeDimmerFeature(
                dimmerFeature, featureInvocationMock, null);

        then(behaviour).should().apply(featureInvocationMock);
    }

    @Test
    public void does_not_allow_ALWAYS_OFF_when_put_featureWithBehaviour() {
        exception.expect(IllegalArgumentException.class);
        exception.expectMessage(
                String.format("Value %s for feature not allowed", ALWAYS_OFF));

        dimmerProcessor.featureWithBehaviour(ALWAYS_OFF, s -> "");

    }

    @Test
    public void does_not_allow_ALWAYS_OFF_when_put_featureWithDefaultException() {
        exception.expect(IllegalArgumentException.class);
        exception.expectMessage(
                String.format("Value %s for feature not allowed", ALWAYS_OFF));

        dimmerProcessor.featureWithBehaviour(ALWAYS_OFF, s -> "");

    }

    @Test
    public void does_not_allow_ALWAYS_OFF_when_put_featureWithException() {
        exception.expect(IllegalArgumentException.class);
        exception.expectMessage(
                String.format("Value %s for feature not allowed", ALWAYS_OFF));

        dimmerProcessor.featureWithBehaviour(ALWAYS_OFF, s -> "");

    }

    @Test
    public void does_not_allow_ALWAYS_OFF_when_put_featureWithValue() {
        exception.expect(IllegalArgumentException.class);
        exception.expectMessage(
                String.format("Value %s for feature not allowed", ALWAYS_OFF));

        dimmerProcessor.featureWithBehaviour(ALWAYS_OFF, s -> "");
    }

}
