package com.github.cloudyrock.dimmer;

import java.util.function.Function;

final class FeatureMetadataBehaviour extends FeatureMetadata{

    private final Function<FeatureInvocation, Object> behaviour;

    FeatureMetadataBehaviour(String feature,
                             String operation,
                             Function<FeatureInvocation, Object> behaviour) {
        super(feature, operation);
        this.behaviour = behaviour;
    }

    Function<FeatureInvocation, Object> getBehaviour() {
        return behaviour;
    }
}
