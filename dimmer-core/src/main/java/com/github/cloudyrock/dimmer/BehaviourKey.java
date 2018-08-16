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
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        BehaviourKey that = (BehaviourKey) o;

        if (!feature.equals(that.feature)) return false;
        return operation.equals(that.operation);
    }

    @Override
    public int hashCode() {
        int result = feature.hashCode();
        result = 31 * result + operation.hashCode();
        return result;
    }
}
