package com.github.cloudyrock.dimmer;

import com.github.cloudyrock.dimmer.exceptions.DimmerConfigException;

public final class FeatureServerExecutor extends FeatureProcessorBase {

    Object executeDimmerFeature(String feature,
                                FeatureInvocation featureInvocation){

        //TODO: add an option of running without FeatureInvocation?

        if (isFeatureConfigured(feature)) {
            return runFeature(feature, featureInvocation);
        } else {
            return new DimmerConfigException("Feature not found for environment");
        }
    }
}
