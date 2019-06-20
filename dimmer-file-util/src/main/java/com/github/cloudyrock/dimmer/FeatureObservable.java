package com.github.cloudyrock.dimmer;

import java.util.function.Consumer;

public interface FeatureObservable {
    void subscribe(Consumer<FeatureUpdateEvent> observer);
}
