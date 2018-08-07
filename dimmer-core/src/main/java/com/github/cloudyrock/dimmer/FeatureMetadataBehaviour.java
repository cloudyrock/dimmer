package com.github.cloudyrock.dimmer;

import java.util.function.Function;

public final class FeatureMetadataBehaviour extends FeatureMetadata{

    private final Function<FeatureInvocationBase, ?> behaviour;

    public FeatureMetadataBehaviour(String feature,
                                    Function<FeatureInvocationBase, ?> behaviour) {
        super(feature);
        this.behaviour = behaviour;
    }

    public Function<FeatureInvocationBase, ?> getBehaviour() {
        return behaviour;
    }
}
