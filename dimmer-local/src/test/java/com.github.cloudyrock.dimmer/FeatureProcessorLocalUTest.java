package com.github.cloudyrock.dimmer;

import com.github.cloudyrock.dimmer.displayname.DisplayName;
import com.github.cloudyrock.dimmer.exceptions.DummyExceptionWithFeatureInvocation;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.Mock;

import java.util.UUID;
import java.util.function.Function;

import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertEquals;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.MockitoAnnotations.initMocks;

public class FeatureProcessorLocalUTest {
    @Rule public ExpectedException exception = ExpectedException.none();


    protected FeatureProcessorLocal FeatureProcessorLocal;

    @Mock
    private Function<FeatureInvocation, String> behaviour;

    @Mock
    private FeatureInvocation featureInvocationMock;

    private String feature;

    @Before
    public void setUp() {

        FeatureProcessorLocal = new FeatureProcessorLocal();
        feature = "FEATURE" + UUID.randomUUID().toString();
        initMocks(this);
        given(featureInvocationMock.getReturnType()).willReturn(String.class);
    }


    @Test
    @DisplayName("Should run behaviour when FEATURE when featureWithBehaviour")
    public void featureAndConfiguredWithBehaviour() throws Throwable {
        FeatureProcessorLocal.featureWithBehaviour(feature, s -> "VALUE");
        Object actualResult = FeatureProcessorLocal.executeDimmerFeature(
                feature, featureInvocationMock, null);
        assertEquals("VALUE", actualResult);
    }

    @Test
    @DisplayName("Should return value when featureWithValue")
    public void featureAndConfiguredWithValue() throws Throwable {
        FeatureProcessorLocal.featureWithValue(feature, "VALUE");
        Object actualResult = FeatureProcessorLocal.executeDimmerFeature(
                feature, featureInvocationMock, null);
        assertEquals("VALUE", actualResult);
    }

    //TODO replace this test
//    @Test(expected = DefaultException.class)
//    @DisplayName("Should throw default exception when featureWithDefaultException")
//    public void featureAndConfiguredWithDefaultException() throws Throwable {
//        FeatureProcessorLocal.featureWithDefaultException(feature);;
//        FeatureProcessorLocal.executeDimmerFeature(feature, featureInvocationMock, null);
//    }

    @Test
    @DisplayName("Should pass FeatureInvocation parameter when featureWithBehaviour")
    public void ensureFeatureInvocationParameterWhenBehaviour() throws Throwable {
        given(behaviour.apply(any(FeatureInvocation.class))).willReturn("not_checked");

        FeatureProcessorLocal.featureWithBehaviour(feature, behaviour);
        FeatureProcessorLocal.executeDimmerFeature(feature, featureInvocationMock, null);

        then(behaviour).should().apply(featureInvocationMock);
    }

    @Test
    @DisplayName("Should pass FeatureInvocation parameter when featureWithBehaviour")
    public void ensureFeatureInvocationParameterWhenException() throws Throwable {

        FeatureInvocation featureInvocationMock = mock(FeatureInvocation.class);
        given(behaviour.apply(any(FeatureInvocation.class))).willReturn("not_checked");
        

        FeatureProcessorLocal.featureWithException(
                feature,
                DummyExceptionWithFeatureInvocation.class);

        exception.expect(DummyExceptionWithFeatureInvocation.class);
        exception.expect(hasProperty("featureInvocation", is(featureInvocationMock)));

        FeatureProcessorLocal.executeDimmerFeature(feature, featureInvocationMock, null);
    }
}
