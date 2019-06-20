package com.github.cloudyrock.dimmer.builder;

import com.github.cloudyrock.dimmer.FeatureInvocation;
import com.github.cloudyrock.dimmer.FeatureObservable;
import com.github.cloudyrock.dimmer.metadata.FeatureMetadata;

import java.util.Set;

public class DummyFeatureProcessor extends FeatureExecutorImpl {

    DummyFeatureProcessor(FeatureObservable featureObservable,
                          Set<FeatureMetadata> featureActions,
                          Class<? extends RuntimeException> defaultException) {
        super(featureObservable, featureActions, defaultException);
    }

    public Object executeDimmerFeature(String feature,
                                       String operation,
                                       FeatureInvocation featureInvocation) {
        return runFeature(feature, operation, featureInvocation);
    }

}
