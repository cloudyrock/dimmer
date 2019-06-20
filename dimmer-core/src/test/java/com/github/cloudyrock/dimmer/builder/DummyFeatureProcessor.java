package com.github.cloudyrock.dimmer.builder;

import com.github.cloudyrock.dimmer.FeatureInvocation;

import static org.mockito.Mockito.mock;

import static org.mockito.Mockito.mock;

public class DummyFeatureProcessor extends FeatureExecutorImpl {

    public DummyFeatureProcessor() {
        super(mock(FeatureBroker.class));
    }

    public Object executeDimmerFeature(String feature,
                                       String operation,
                                       FeatureInvocation featureInvocation) {
        return executeFeature(feature, operation, featureInvocation);
    }

}
