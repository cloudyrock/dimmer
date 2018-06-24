package com.github.cloudyrock.dimmer;

import com.github.cloudyrock.dimmer.exceptions.DefaultException;
import com.github.cloudyrock.dimmer.exceptions.DummyException;
import com.github.cloudyrock.dimmer.displayname.DisplayName;
import com.github.cloudyrock.dimmer.exceptions.DummyNoConstructorException;
import com.github.cloudyrock.dimmer.exceptions.ExceptionWithFeatureInvocation;
import org.aspectj.lang.ProceedingJoinPoint;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.util.UUID;
import java.util.function.Function;

import static org.hamcrest.Matchers.hasProperty;

import static com.github.cloudyrock.dimmer.DimmerFeature.ALWAYS_OFF;
import static com.github.cloudyrock.dimmer.DimmerFeature.DimmerBehaviour.DEFAULT;
import static com.github.cloudyrock.dimmer.DimmerFeature.DimmerBehaviour.RETURN_NULL;
import static com.github.cloudyrock.dimmer.DimmerFeature.DimmerBehaviour.THROW_EXCEPTION;
import static com.github.cloudyrock.dimmer.DimmerProcessor.DIMMER_RETURN_TYPE_EXCEPTION_MESSAGE;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class DimmerProcessorUTest extends DimmerTestBase {

    @Rule
    public ExpectedException exception = ExpectedException.none();

    @Mock
    private DimmerFeature dimmerFeature;

    @Mock
    private ProceedingJoinPoint jointPoint;

    @Mock
    private Function<FeatureInvocation, String> behaviour;

    @Mock
    private FeatureInvocation featureInvocation;

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

    private void givenDimmerFeature(String value,
                                    DimmerFeature.DimmerBehaviour behaviour,
                                    boolean disabled) {
        givenDimmerFeatureWithEx(value, behaviour, disabled,
                DimmerFeature.NULL_EXCEPTION.class);
    }

    @Test
    @DisplayName("Should return null when ALWAYS_OFF and behaviour is RETURN_NULL")
    public void alwaysOffAndReturnNull() throws Throwable {
        givenDimmerFeature(ALWAYS_OFF, RETURN_NULL, false);

        Object returnedValue = dimmerProcessor.executeDimmerFeature(
                dimmerFeature, null, null);

        assertNull(returnedValue);
    }

    @Test(expected = DefaultException.class)
    @DisplayName("Should throw default exception when ALWAYS_OFF and behaviour is THROW_EXCEPTION")
    public void alwaysOffAndThrowException() throws Throwable {
        givenDimmerFeature(ALWAYS_OFF, THROW_EXCEPTION, false);
        dimmerProcessor.executeDimmerFeature(
                dimmerFeature, null, null);
    }

    @Test(expected = DummyException.class)
    @DisplayName("Should throw DummyException when ALWAYS_OFF and behaviour is THROW_EXCEPTION and exception is DummyException")
    public void alwaysOffAndThrowCustomException()
            throws Throwable {
        givenDimmerFeatureWithEx(ALWAYS_OFF, THROW_EXCEPTION, false,
                DummyException.class);
        dimmerProcessor.executeDimmerFeature(
                dimmerFeature, null, null);
    }

    @Test(expected = DefaultException.class)
    @DisplayName("Should throw default exception when ALWAYS_OFF and behaviour is DEFAULT")
    public void alwaysOffAndDefault() throws Throwable {
        givenDimmerFeature(ALWAYS_OFF, DEFAULT, false);
        dimmerProcessor.executeDimmerFeature(
                dimmerFeature, null, null);
    }

    @Test
    @DisplayName("Should call real method when ALWAYS_OFF and disabled is true")
    public void alwaysOffAndDisabled() throws Throwable {
        givenDimmerFeature(ALWAYS_OFF, DEFAULT, true);
        given(jointPoint.proceed()).willReturn("REAL METHOD");

        Object actualValue = dimmerProcessor.executeDimmerFeature(
                dimmerFeature, null, jointPoint);

        then(jointPoint).should().proceed();
        assertEquals(actualValue, "REAL METHOD");
    }

    @Test
    @DisplayName("Should run real method when FEATURE and not registered in DimmerProcessor")
    public void featureButNotRegistered() throws Throwable {
        givenDimmerFeature(feature, DEFAULT, false);
        given(jointPoint.proceed()).willReturn("REAL METHOD");

        Object actualValue = dimmerProcessor.executeDimmerFeature(
                dimmerFeature, null, jointPoint);

        then(jointPoint).should().proceed();
        assertEquals(actualValue, "REAL METHOD");
    }

    @Test
    @DisplayName("Should return null when FEATURE and behaviour is RETURN_NULL")
    public void featureAndReturnNull() throws Throwable {
        dimmerProcessor.featureWithValue(feature, "VALUE");
        givenDimmerFeature(feature, RETURN_NULL, false);

        Object returnedValue = dimmerProcessor.executeDimmerFeature(
                dimmerFeature, null, null);

        assertNull(returnedValue);
    }

    @Test(expected = DefaultException.class)
    @DisplayName("Should throw default exception when FEATURE and behaviour is THROW_EXCEPTION")
    public void featureAndThrowDefaultException() throws Throwable {
        dimmerProcessor.featureWithValue(feature, "VALUE");
        givenDimmerFeature(feature, THROW_EXCEPTION, false);

        dimmerProcessor.executeDimmerFeature(
                dimmerFeature, null, null);
    }

    @Test
    @DisplayName("Should call real method when FEATURE and disabled is true")
    public void featureAndDisabled() throws Throwable {
        dimmerProcessor.featureWithValue(feature, "VALUE");
        givenDimmerFeature(feature, DEFAULT, true);
        given(jointPoint.proceed()).willReturn("REAL METHOD");

        Object actualValue = dimmerProcessor.executeDimmerFeature(
                dimmerFeature, null, jointPoint);

        then(jointPoint).should().proceed();
        assertEquals(actualValue, "REAL METHOD");
    }

    @Test
    @DisplayName("Should run behaviour when FEATURE and behaviour is DEFAULT")
    public void featureAndConfiguredWithBehaviour() throws Throwable {

        when(featureInvocation.getReturnType()).thenReturn(new String());
        when(jointPoint.getTarget()).thenReturn(new String());

        dimmerProcessor.featureWithBehaviour(feature, s -> "VALUE");
        givenDimmerFeature(feature, DEFAULT, false);
        Object actualResult = dimmerProcessor.executeDimmerFeature(
                dimmerFeature, featureInvocation, jointPoint);
        assertEquals("VALUE", actualResult);
    }

    @Test
    @DisplayName("Should return value when FEATURE is configured to return value and behaviour is DEFAULT")
    public void featureAndConfiguredWithValue() throws Throwable {

        when(featureInvocation.getReturnType()).thenReturn(new String());
        when(jointPoint.getTarget()).thenReturn(new String());

        dimmerProcessor.featureWithValue(feature, "VALUE");
        givenDimmerFeature(feature, DEFAULT, false);
        Object actualResult = dimmerProcessor.executeDimmerFeature(
                dimmerFeature, featureInvocation, jointPoint);
        assertEquals("VALUE", actualResult);
    }

    @Test(expected = DefaultException.class)
    @DisplayName("Should throw default exception when FEATURE is configured to throw default exception and behaviour is DEFAULT")
    public void featureAndConfiguredWithDefaultException() throws Throwable {

        when(featureInvocation.getReturnType()).thenReturn(new String());
        when(jointPoint.getTarget()).thenReturn(new String());

        dimmerProcessor.featureWithDefaultException(feature);
        givenDimmerFeature(feature, DEFAULT, false);
        dimmerProcessor.executeDimmerFeature(dimmerFeature, featureInvocation, jointPoint);
    }

    @Test(expected = DimmerConfigException.class)
    public void
    when_exception_doesnt_provide_right_constructors_should_DimmerConfigException()
            throws Throwable {
        dimmerProcessor.featureWithDefaultException(feature);
        givenDimmerFeatureWithEx(feature, THROW_EXCEPTION, false,
                DummyNoConstructorException.class);
        dimmerProcessor.executeDimmerFeature(dimmerFeature, null, null);
    }

    @Test
    @DisplayName("Should pass FeatureInvocation to an exception with FeatureInvocation as Parameter")
    public void ensureExceptionWithFeatureInvocation() throws Throwable {
        dimmerProcessor.featureWithDefaultException(feature);
        givenDimmerFeatureWithEx(feature, THROW_EXCEPTION, false,
                ExceptionWithFeatureInvocation.class);

        exception.expect(ExceptionWithFeatureInvocation.class);
        exception.expect(hasProperty("featureInvocation", is(featureInvocation)));

        dimmerProcessor.executeDimmerFeature(dimmerFeature, featureInvocation, null);
    }

    @Test
    @DisplayName("Should pass FeatureInvocation parameter to behaviour")
    public void ensureFeatureInvocationParameter() throws Throwable {

        when(featureInvocation.getReturnType()).thenReturn(new String());
        when(jointPoint.getTarget()).thenReturn(new String());

        given(behaviour.apply(any(FeatureInvocation.class))).willReturn("BEHAVIOUR");
        givenDimmerFeature(feature, DEFAULT, false);

        dimmerProcessor.featureWithBehaviour(feature, behaviour);
        dimmerProcessor.executeDimmerFeature(
                dimmerFeature, featureInvocation, jointPoint);

        then(behaviour).should().apply(featureInvocation);
    }

    @Test
    @DisplayName(value = "Should throw  IllegalArgumentException when using ALWAYS_OFF as feature when configuring DimmerProcessor.featureWithBehaviour(...)")
    public void featureWithBehaviourDoesNotAllowALWAYS_OFF() {
        exception.expect(IllegalArgumentException.class);
        exception.expectMessage(
                String.format("Value %s for feature not allowed", ALWAYS_OFF));

        dimmerProcessor.featureWithBehaviour(ALWAYS_OFF, s -> "");

    }

    @Test
    @DisplayName(value = "Should throw  IllegalArgumentException when using ALWAYS_OFF as feature when configuring DimmerProcessor.featureWithDefaultException(...)")
    public void featureWithDefaultExceptionDoesNotAllowALWAYS_OFF() {
        exception.expect(IllegalArgumentException.class);
        exception.expectMessage(
                String.format("Value %s for feature not allowed", ALWAYS_OFF));

        dimmerProcessor.featureWithDefaultException(ALWAYS_OFF);

    }

    @Test
    @DisplayName(value = "Should throw  IllegalArgumentException when using ALWAYS_OFF as feature when configuring DimmerProcessor.featureWithException(...)")
    public void featureWuthExceptionDoesNotAllowALWAYS_OFF() {
        exception.expect(IllegalArgumentException.class);
        exception.expectMessage(
                String.format("Value %s for feature not allowed", ALWAYS_OFF));

        dimmerProcessor.featureWithException(ALWAYS_OFF, DummyException.class);

    }

    @Test
    @DisplayName(value = "Should throw  IllegalArgumentException when using ALWAYS_OFF as feature when configuring DimmerProcessor.featureWithValue(...)")
    public void featureWithValueDoesNotAllowALWAYS_OFF() {
        exception.expect(IllegalArgumentException.class);
        exception.expectMessage(
                String.format("Value %s for feature not allowed", ALWAYS_OFF));

        dimmerProcessor.featureWithValue(ALWAYS_OFF, "VALUE");
    }


    @Test(expected = DimmerConfigException.class)
    @DisplayName(value = "Should throw DimmerConfigException if class types between config and invocation are different")
    public void when_differentClassTypesConfigurationException() throws Throwable {

        when(featureInvocation.getReturnType()).thenReturn(new String());
        when(jointPoint.getTarget()).thenReturn(new Integer(0));

        dimmerProcessor.featureWithBehaviour(feature, s -> "VALUE");
        givenDimmerFeature(feature, DEFAULT, false);

        try {
            dimmerProcessor.executeDimmerFeature(dimmerFeature, featureInvocation, jointPoint);
        } catch (DimmerConfigException e) {
            if (e.getMessage().equals(DIMMER_RETURN_TYPE_EXCEPTION_MESSAGE)) {
                throw e;
            } else {
                throw new RuntimeException("Unexpected exception message");
            }
        }
    }

    @Test
    @DisplayName(value = "Should not throw any exception if class types and subtypes are matching")
    public void when_hierarchicalClassTypesMatch_execute() throws Throwable {

        when(featureInvocation.getReturnType()).thenReturn(new Object());
        when(jointPoint.getTarget()).thenReturn(new Integer(0));

        dimmerProcessor.featureWithBehaviour(feature, s -> "VALUE");
        givenDimmerFeature(feature, DEFAULT, false);
        dimmerProcessor.executeDimmerFeature(dimmerFeature, featureInvocation, jointPoint);
    }

}
