package com.github.cloudyrock.dimmer;

class BehaviourKey {

    private static final String DEFAULT_OPERATION = "";
    private final String feature;
    private final String operation;

    BehaviourKey(String feature, String operation) {
        if(feature == null || feature.isEmpty()) {
            throw new IllegalArgumentException("Feature cannot be null");
        }
        this.feature = feature;
        this.operation = operation != null ? operation : DEFAULT_OPERATION;
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

        BehaviourKey that = (BehaviourKey) o;

        return feature.equals(that.feature) && operation.equals(that.operation);
    }

    @Override
    public int hashCode() {
        return 31 * feature.hashCode() + operation.hashCode();
    }
}
