package com.github.cloudyrock.dimmer.builder;

import com.github.cloudyrock.dimmer.FeatureInvocation;

public class DummyFeatureProcessor extends FeatureExecutorImpl {

    DummyFeatureProcessor() {
    }

    public Object executeDimmerFeature(String feature,
                                       String operation,
                                       FeatureInvocation featureInvocation) {
        return runFeature(feature, operation, featureInvocation);
    }

}
