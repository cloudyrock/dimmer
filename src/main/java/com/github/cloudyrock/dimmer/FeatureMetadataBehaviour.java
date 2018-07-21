package com.github.cloudyrock.dimmer;

import java.util.function.Function;

public final class FeatureMetadataBehaviour extends FeatureMetadata{

    private final Function<String, Object> behaviour;

    public FeatureMetadataBehaviour(Function<String, Object> behaviour) {
        this.behaviour = behaviour;
    }

    public Function<String, Object> getBehaviour() {
        return behaviour;
    }
}
