package com.github.cloudyrock.dimmer.logic;

import com.github.cloudyrock.dimmer.DimmerInvocationException;
import com.github.cloudyrock.dimmer.FeatureInvocation;

import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

class FeatureExecutionPlan {

    private final Set<String> toggledOnFeatures;
    private final Map<Behaviour.BehaviourKey, Function<FeatureInvocation, ?>> behaviours;
    private final Class<? extends RuntimeException> defaultException;

    FeatureExecutionPlan(Set<String> toggledOnFeatures,
                         Map<Behaviour.BehaviourKey, Function<FeatureInvocation, ?>> behaviours) {
        this.toggledOnFeatures = toggledOnFeatures.stream().map(String::toLowerCase).collect(Collectors.toSet());
        this.behaviours = behaviours;
        this.defaultException = DimmerInvocationException.class; //todo changes this to take it from config
    }

    boolean isFeatureToggledOn(String feature) {
        return toggledOnFeatures.contains(feature.toLowerCase());
    }

    Map<Behaviour.BehaviourKey, Function<FeatureInvocation, ?>> getBehaviours() {
        return behaviours;
    }

    boolean isThereBehaviourFor(String feature, String operation) {
        return behaviours.containsKey(new Behaviour.BehaviourKey(feature, operation));
    }

    Class<? extends RuntimeException> getDefaultExceptionType() {
        return defaultException;
    }

}
