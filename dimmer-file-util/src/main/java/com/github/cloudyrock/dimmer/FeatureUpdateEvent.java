package com.github.cloudyrock.dimmer;

import java.util.HashSet;
import java.util.Set;

public final class FeatureUpdateEvent {

    private final Set<String> featuresToggledOn;

    FeatureUpdateEvent(Set<String> featuresToggledOn) {
        Preconditions.checkNullOrEmpty(featuresToggledOn, "Features toggled on in new event");
        this.featuresToggledOn = featuresToggledOn;
    }

    public Set<String> getFeaturesToggledOn() {
        return new HashSet<>(featuresToggledOn);
    }

}
