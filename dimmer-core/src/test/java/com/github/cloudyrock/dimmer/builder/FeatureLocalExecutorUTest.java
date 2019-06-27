package com.github.cloudyrock.dimmer.builder;

import com.github.cloudyrock.dimmer.DisplayName;
import com.github.cloudyrock.dimmer.FeatureInvocation;
import com.github.cloudyrock.dimmer.MethodCaller;
import com.github.cloudyrock.dimmer.metadata.BehaviourKey;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.Mock;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.function.Function;

import static org.junit.Assert.assertEquals;
import static org.mockito.BDDMockito.given;
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
    @DisplayName("Should run behaviour")
    public void shouldRun() throws Throwable {

        Map<BehaviourKey, Function<FeatureInvocation, ?>> behaviours = new HashMap<>();
        behaviours.put(new BehaviourKey(feature, operation), s -> "VALUE");
        dimmerProcessor.updateBehaviours(behaviours);
        Object actualResult = dimmerProcessor.executeDimmerFeature(
                feature, operation, featureInvocation, methodCaller);
        assertEquals("VALUE", actualResult);
    }
}
