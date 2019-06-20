package com.github.cloudyrock.dimmer;

import java.util.Set;
import java.util.function.Consumer;

public class StaticLocalFeatureObservable implements FeatureObservable {

    private final Set<String> featuresToggledOff;

    public StaticLocalFeatureObservable(Set<String> featuresToggledOff) {
        this.featuresToggledOff = featuresToggledOff;
    }

    @Override
    public void observe(Consumer<FeatureUpdateEvent> observer) {
        observer.accept(new FeatureUpdateEvent(featuresToggledOff));
    }
}
