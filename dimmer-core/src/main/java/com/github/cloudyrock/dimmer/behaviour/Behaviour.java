package com.github.cloudyrock.dimmer.behaviour;

import com.github.cloudyrock.dimmer.FeatureInvocation;
import com.github.cloudyrock.dimmer.Preconditions;

import java.util.function.Function;

public class Behaviour {


    private final String feature;
    private final String operation;
    private final Function<FeatureInvocation, Object> behaviour;

    public Behaviour(String feature,
                     String operation,
                     Function<FeatureInvocation, Object> behaviour) {
        checkProperty(feature, "Feature");
        this.feature = feature;
        this.operation = operation != null ? operation : "";
        this.behaviour = behaviour;
    }


    void checkProperty(String feature, String propertyName1) {
        if(feature == null || feature.isEmpty()) {
            throw new IllegalArgumentException(propertyName1 + " cannot be empty");
        }
    }

    public String getFeature() {
        return feature;
    }

    public String getOperation() {
        return operation;
    }

    public Function<FeatureInvocation, Object> getBehaviour() {
        return behaviour;
    }

    public BehaviourKey getKey() {
        return new BehaviourKey(getFeature(), getOperation());
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        final Behaviour that = (Behaviour) o;

        return feature.equals(that.feature) && operation.equals(that.operation);
    }


    @Override
    public int hashCode() {
        return 31 * feature.hashCode() + operation.hashCode();
    }

    public static class BehaviourKey {

        private final String feature;
        private final String operation;

        public BehaviourKey(String feature,
                         String operation) {
            Preconditions.checkNullOrEmpty(feature, "feature");
            this.feature = feature;
            this.operation = operation != null ? operation : "";
        }

        public String getFeature() {
            return feature;
        }

        public String getOperation() {
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
