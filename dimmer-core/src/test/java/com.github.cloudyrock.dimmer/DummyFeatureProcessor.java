package com.github.cloudyrock.dimmer;

public class DummyFeatureProcessor extends FeatureProcessorBase {

    public Object executeDimmerFeature(String feature,
                                       FeatureInvocationBase featureInvocation) {
        return runFeature(feature, featureInvocation);
    }

}
