package com.github.cloudyrock.dimmer;

class BehaviourKey {

    private final String feature;
    private final String operation;

    BehaviourKey(String feature, String operation) {
        if(feature == null || feature.isEmpty()) {
            throw new IllegalArgumentException("Feature cannot be empty");
        }
        if(operation == null || operation.isEmpty()) {
            throw new IllegalArgumentException("Operation cannot be empty");
        }
        this.feature = feature;
        this.operation = operation;
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
