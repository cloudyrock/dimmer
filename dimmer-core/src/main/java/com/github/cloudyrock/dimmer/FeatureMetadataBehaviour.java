package com.github.cloudyrock.dimmer;

import java.util.function.Function;

public final class FeatureMetadataBehaviour extends FeatureMetadata{

    private final Function<FeatureInvocation, ?> behaviour;

    public FeatureMetadataBehaviour(String feature,
                                    String operation,
                                    Function<FeatureInvocation, ?> behaviour) {
        super(feature, operation);
        this.behaviour = behaviour;
    }

    public Function<FeatureInvocation, ?> getBehaviour() {
        return behaviour;
    }
}
