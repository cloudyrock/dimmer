package com.github.cloudyrock.dimmer;

public class DummyFeatureProcessor extends FeatureProcessorBase {

    public Object executeDimmerFeature(String feature,
                                       String operation,
                                       FeatureInvocation featureInvocation) {
        return runFeature(feature, operation, featureInvocation);
    }

}
