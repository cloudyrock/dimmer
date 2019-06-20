package com.github.cloudyrock.dimmer.builder;

import com.github.cloudyrock.dimmer.*;
import com.github.cloudyrock.dimmer.DisplayName;
import com.github.cloudyrock.dimmer.FeatureInvocation;
import com.github.cloudyrock.dimmer.MethodCaller;
import com.github.cloudyrock.dimmer.metadata.BehaviourKey;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.Mock;

import java.util.HashSet;
import java.util.HashMap;
import java.util.Map;
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

public class FeatureLocalExecutorUTest {
    @Rule
    public ExpectedException exception = ExpectedException.none();

    private static final String operation = "operation";

    private FeatureExecutorImpl dimmerProcessor;

    @Mock
    private Function<FeatureInvocation, String> behaviour;

    @Mock
    private MethodCaller methodCaller;

    private String feature;

    private FeatureInvocation featureInvocation;

    @Before
    public void setUp() throws Throwable {

        dimmerProcessor = new FeatureExecutorImpl(mock(FeatureBroker.class));

        dimmerProcessor = new FeatureExecutorImpl(new StaticLocalFeatureObservable(new HashSet<>()), new HashSet<>(), RuntimeException.class);
        dimmerProcessor.start();
        feature = "FEATURE" + UUID.randomUUID().toString();
        initMocks(this);
        given(methodCaller.call()).willReturn(String.class);

        featureInvocation = new FeatureInvocationImpl(
                feature,
                operation,
                "method",
                FeatureLocalExecutorUTest.class,
                new Object[]{"value1"},
                String.class
        );
    }

    @Test
    @DisplayName("Should run behaviour when FEATURE when featureWithBehaviour")
    public void shouldRunBehaviour() throws Throwable {
        dimmerProcessor.featureWithBehaviour(feature, operation, s -> "VALUE");
        Object actualResult = dimmerProcessor.executeDimmerFeature(
                feature, operation, featureInvocation, methodCaller);
        assertEquals("VALUE", actualResult);
    }

    @Test
    @DisplayName("Should return value when featureWithValue")
    public void shouldReturnValue() throws Throwable {
        dimmerProcessor.featureWithValue(feature, operation, "VALUE");
        Object actualResult = dimmerProcessor.executeDimmerFeature(
                feature, operation, featureInvocation, methodCaller);
        assertEquals("VALUE", actualResult);
    }

    @Test
    @DisplayName("Should pass FeatureInvocation parameter when featureWithBehaviour")
    public void ensureFeatureInvocationParameterWhenBehaviour() throws Throwable {
        given(behaviour.apply(any(FeatureInvocation.class))).willReturn("not_checked");

        dimmerProcessor.featureWithBehaviour(feature, operation, behaviour);
        dimmerProcessor.executeDimmerFeature(feature, operation, featureInvocation, methodCaller);

        then(behaviour).should().apply(featureInvocation);
    }

    @Test
    @DisplayName("Should pass FeatureInvocation parameter when featureWithBehaviour")
    public void ensureFeatureInvocationParameterWhenException() throws Throwable {
        dimmerProcessor.featureWithException(
                feature,
                operation,
                DummyExceptionWithFeatureInvocation.class);

        exception.expect(DummyExceptionWithFeatureInvocation.class);
        exception.expect(hasProperty("featureInvocation", is(featureInvocation)));

        dimmerProcessor.executeDimmerFeature(feature, operation, featureInvocation, methodCaller);
    }
}
