package com.github.cloudyrock.dimmer;

import java.util.Set;

public class DummyFeatureProcessor extends FeatureProcessorBase {

    DummyFeatureProcessor() {
    }

    DummyFeatureProcessor(Set<FeatureMetadata> metadata) {
        super(metadata, DimmerInvocationException.class);
    }

    public Object executeDimmerFeature(String feature,
                                       String operation,
                                       FeatureInvocation featureInvocation) {
        return runFeature(feature, operation, featureInvocation);
    }

}
