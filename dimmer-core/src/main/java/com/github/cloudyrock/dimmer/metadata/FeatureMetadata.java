package com.github.cloudyrock.dimmer.metadata;

import com.github.cloudyrock.dimmer.FeatureInvocation;

import java.util.function.Function;

public abstract class FeatureMetadata extends FeatureOperationBase{

    FeatureMetadata(String feature, String operation) {
        super(feature, operation);
    }


    public BehaviourKey getKey() {
        return new BehaviourKey(getFeature(), getOperation());
    }

    public abstract Function<FeatureInvocation, Object> getFunction();

}
