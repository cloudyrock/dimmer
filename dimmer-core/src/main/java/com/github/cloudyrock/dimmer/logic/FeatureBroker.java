package com.github.cloudyrock.dimmer.logic;


import com.github.cloudyrock.dimmer.DimmerLogger;
import com.github.cloudyrock.dimmer.FeatureInvocation;
import com.github.cloudyrock.dimmer.FeatureObservable;
import com.github.cloudyrock.dimmer.FeatureUpdateEvent;

import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;

class FeatureBroker {


    private static final DimmerLogger logger = new DimmerLogger(FeatureBroker.class);

    private final FeatureObservable featureObservable;
    private final Set<Behaviour> featureActions;
    private final Class<? extends RuntimeException> defaultException;

    private Consumer<Map<Behaviour.BehaviourKey, Function<FeatureInvocation, ?>>> subscriber;

    FeatureBroker(FeatureObservable featureObservable,
                  Set<Behaviour> featureActions,
                  Class<? extends RuntimeException> defaultException) {
        this.featureActions = featureActions;
        this.featureObservable = featureObservable;
        this.defaultException = defaultException;
    }

    void start() {
        featureObservable.subscribe(this::process);
    }

    void setSubscriber(Consumer<Map<Behaviour.BehaviourKey, Function<FeatureInvocation, ?>>> subscriber) {
        this.subscriber = subscriber;
    }


    void process(FeatureUpdateEvent featureUpdateEvent) {
        if (featureActions != null && subscriber != null) {
            final Map<Behaviour.BehaviourKey, Function<FeatureInvocation, ?>> behaviours =
            featureActions.stream()
                    .filter(fm-> featureUpdateEvent.getFeaturesToggledOn().contains(fm.getKey().getFeature()))
                    .peek(fm -> logger.info("APPLIED feature [{}]", fm.toString()))
                    .collect(Collectors.toMap(Behaviour::getKey, Behaviour::getBehaviour));
            subscriber.accept(behaviours);
        }

    }
}
