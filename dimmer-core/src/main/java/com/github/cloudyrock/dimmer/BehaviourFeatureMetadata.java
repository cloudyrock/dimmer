package com.github.cloudyrock.dimmer;

import java.util.function.Function;

final class BehaviourFeatureMetadata extends FeatureMetadata{

    private final Function<FeatureInvocation, Object> behaviour;

    BehaviourFeatureMetadata(String feature,
                             String operation,
                             Function<FeatureInvocation, Object> behaviour) {
        super(feature, operation);
        this.behaviour = behaviour;
    }

    Function<FeatureInvocation, Object> getBehaviour() {
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
