package com.github.cloudyrock.dimmer;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;

public class StaticLocalFeatureObservable implements FeatureObservable {

    private final Set<String> featuresToggledOff;

    public StaticLocalFeatureObservable(Set<String> featuresToggledOff) {
        this.featuresToggledOff = featuresToggledOff;
    }

    @Override
    public void subscribe(Consumer<FeatureUpdateEvent> observer) {
        observer.accept(new FeatureUpdateEvent(featuresToggledOff));
    }
}
