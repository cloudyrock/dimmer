package com.github.cloudyrock.dimmer.builder;

import com.github.cloudyrock.dimmer.FeatureInvocation;
import com.github.cloudyrock.dimmer.Preconditions;

import java.util.function.Function;

class Behaviour {


    private final BehaviourKey key;
    private final Function<FeatureInvocation, Object> behaviour;

    Behaviour(String feature,
              String operation,
              Function<FeatureInvocation, Object> behaviour) {
        this.key = new BehaviourKey(feature, operation);
        this.behaviour = behaviour;
    }

    BehaviourKey getKey() {
        return key;
    }

    Function<FeatureInvocation, Object> getBehaviour() {
        return behaviour;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        return this.key.equals(((Behaviour) o).key);
    }


    @Override
    public int hashCode() {
        return key.hashCode();
    }

    public static class BehaviourKey {

        private final String feature;
        private final String operation;

        BehaviourKey(String feature,
                     String operation) {
            Preconditions.checkNullOrEmpty(feature, "feature");
            this.feature = feature;
            this.operation = operation != null ? operation : "";
        }

        String getFeature() {
            return feature;
        }

        String getOperation() {
            return operation;
        }


        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }

            if (o == null || getClass() != o.getClass()) {
                return false;
            }

            final BehaviourKey that = (BehaviourKey) o;

            return feature.equals(that.feature) && operation.equals(that.operation);
        }

        @Override
        public int hashCode() {
            return 31 * feature.hashCode() + operation.hashCode();
        }

    }
}
