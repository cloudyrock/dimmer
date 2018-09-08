package com.github.cloudyrock.dimmer;

import java.util.function.Function;

final class FeatureMetadataBehaviour extends FeatureMetadata{

    private final Function<FeatureInvocation, ?> behaviour;

    FeatureMetadataBehaviour(String feature,
                             String operation,
                             Function<FeatureInvocation, ?> behaviour) {
        super(feature, operation);
        this.behaviour = behaviour;
    }

    Function<FeatureInvocation, ?> getBehaviour() {
        return behaviour;
    }
}
