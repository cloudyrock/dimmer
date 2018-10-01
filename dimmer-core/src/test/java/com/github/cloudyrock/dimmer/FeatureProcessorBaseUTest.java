package com.github.cloudyrock.dimmer;

import com.github.cloudyrock.dimmer.exceptions.DummyExceptionWithFeatureInvocation;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.Mock;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import java.util.function.Function;

import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.MockitoAnnotations.initMocks;

public class FeatureProcessorBaseUTest {
    @Rule public ExpectedException exception = ExpectedException.none();

    private final String operation = "operation";

    protected DummyFeatureProcessor dimmerProcessor;

    @Mock
    private Function<FeatureInvocation, String> behaviour;

    @Mock
    private FeatureInvocation featureInvocationMock;

    private String feature;

    @Before
    public void setUp() {

        dimmerProcessor = new DummyFeatureProcessor();
        feature = "FEATURE" + UUID.randomUUID().toString();
        initMocks(this);
        given(featureInvocationMock.getReturnType()).willReturn(String.class);
    }

    @Test
    @DisplayName("Should run behaviour when FEATURE when featureWithBehaviour")
    public void shouldRunBehaviour() throws Throwable {
        dimmerProcessor.featureWithBehaviour(feature, operation, s -> "VALUE");
        Object actualResult = dimmerProcessor.executeDimmerFeature(
                feature, operation, featureInvocationMock);
        assertEquals("VALUE", actualResult);
    }

    @Test
    @DisplayName("Should return value when featureWithValue")
    public void shouldReturnValue() throws Throwable {
        dimmerProcessor.featureWithValue(feature, operation, "VALUE");
        Object actualResult = dimmerProcessor.executeDimmerFeature(
                feature, operation, featureInvocationMock);
        assertEquals("VALUE", actualResult);
    }

    @Test
    @DisplayName("Should pass FeatureInvocation parameter when featureWithBehaviour")
    public void ensureFeatureInvocationParameterWhenBehaviour() throws Throwable {
        given(behaviour.apply(any(FeatureInvocation.class))).willReturn("not_checked");

        dimmerProcessor.featureWithBehaviour(feature, operation, behaviour);
        dimmerProcessor.executeDimmerFeature(feature, operation, featureInvocationMock);

        then(behaviour).should().apply(featureInvocationMock);
    }

    @Test
    @DisplayName("Should pass FeatureInvocation parameter when featureWithBehaviour")
    public void ensureFeatureInvocationParameterWhenException() throws Throwable {

        FeatureInvocation featureInvocationMock = mock(FeatureInvocation.class);
        given(behaviour.apply(any(FeatureInvocation.class))).willReturn("not_checked");

        dimmerProcessor.featureWithException(
                feature,
                operation,
                DummyExceptionWithFeatureInvocation.class);

        exception.expect(DummyExceptionWithFeatureInvocation.class);
        exception.expect(hasProperty("featureInvocation", is(featureInvocationMock)));

        dimmerProcessor.executeDimmerFeature(feature, operation, featureInvocationMock);
    }

    @Test
    public void constructor() {
        Set<FeatureMetadata> metadata = new HashSet<>();
        metadata.addAll(behaviourMetadataList());
        metadata.addAll(ExceptionMetadataList());
        metadata.addAll(defaultExceptionMetadataList());
        metadata.addAll(valueMetadataList());

        FeatureProcessorBase objTest = new DummyFeatureProcessor(metadata);
        assertTrue(objTest.isConditionPresent("FEATURE-VALUE", "OPERATION-1"));
        assertTrue(objTest.isConditionPresent("FEATURE-DEFAULT-EXCEPTION", "OPERATION-2"));
        assertTrue(objTest.isConditionPresent("FEATURE-EXCEPTION", "OPERATION-2"));
        assertTrue(objTest.isConditionPresent("FEATURE-BEHAVIOUR", "OPERATION-2"));
    }

    private Collection<? extends ValueFeatureMetadata> valueMetadataList() {
        return Arrays.asList(
                new ValueFeatureMetadata("FEATURE-VALUE", "OPERATION-1", "VALUED")
        );
    }

    private Collection<? extends DefaultExceptionFeatureMetadata> defaultExceptionMetadataList() {
        return Arrays.asList(
                new DefaultExceptionFeatureMetadata("FEATURE-DEFAULT-EXCEPTION", "OPERATION-2")
        );
    }

    private Collection<? extends ExceptionFeatureMetadata> ExceptionMetadataList() {
        return Arrays.asList(
                new ExceptionFeatureMetadata("FEATURE-EXCEPTION", "OPERATION-2", RuntimeException.class)
        );
    }

    private Collection<? extends BehaviourFeatureMetadata> behaviourMetadataList() {
        return Arrays.asList(
                new BehaviourFeatureMetadata("FEATURE-BEHAVIOUR", "OPERATION-2",
                        FeatureInvocation::getFeature)
        );
    }
}
