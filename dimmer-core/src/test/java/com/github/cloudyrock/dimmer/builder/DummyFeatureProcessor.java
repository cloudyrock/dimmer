package com.github.cloudyrock.dimmer.builder;

import com.github.cloudyrock.dimmer.FeatureInvocation;
import com.github.cloudyrock.dimmer.FeatureObservable;
import com.github.cloudyrock.dimmer.metadata.FeatureMetadata;

import java.util.Set;

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
