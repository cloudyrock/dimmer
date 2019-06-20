package com.github.cloudyrock.dimmer;

import java.util.HashSet;
import java.util.Set;

public final class FeatureUpdateEvent {

    private final Set<String> featuresToggledOff;

    public FeatureUpdateEvent(Set<String> featuresToggledOff) {
        this.featuresToggledOff = featuresToggledOff;
    }

    public Set<String> getFeaturesToggledOff() {
        return new HashSet<>(featuresToggledOff);
    }

}
