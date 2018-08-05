package com.github.cloudyrock.dimmer;

public class DummyFeatureProcessor extends FeatureProcessorBase {

    DummyFeatureProcessor(DimmerSlf4j logger) {
        super(logger);
    }

    DummyFeatureProcessor() {
        this(DimmerSlf4j.nullLogger());
    }

    public Object executeDimmerFeature(String feature,
                                       FeatureInvocation featureInvocation) {
        return runFeature(feature, featureInvocation);
    }

}
