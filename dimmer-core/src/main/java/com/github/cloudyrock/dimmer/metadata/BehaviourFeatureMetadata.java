package com.github.cloudyrock.dimmer.metadata;

import com.github.cloudyrock.dimmer.FeatureInvocation;

import java.util.function.Function;

public final class BehaviourFeatureMetadata extends FeatureMetadata{

    private final Function<FeatureInvocation, Object> behaviour;

    public BehaviourFeatureMetadata(String feature,
                             String operation,
                             Function<FeatureInvocation, Object> behaviour) {
        super(feature, operation);
        this.behaviour = behaviour;
    }

    public Function<FeatureInvocation, Object> getBehaviour() {
        return behaviour;
    }


    @Override
    public boolean equals(Object o) {
        return super.equals(o);
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }
}
