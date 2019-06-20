package com.github.cloudyrock.dimmer;

import java.util.function.Consumer;

public interface FeatureObservable {
    void observe(Consumer<FeatureUpdateEvent> observer);
}
