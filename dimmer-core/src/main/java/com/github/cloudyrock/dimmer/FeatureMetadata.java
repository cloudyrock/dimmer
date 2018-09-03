package com.github.cloudyrock.dimmer;

abstract class FeatureMetadata {

    private final String feature;
    private final String operation;

    protected FeatureMetadata(String feature, String operation) {
        if (feature == null || feature.isEmpty()) {
            throw new IllegalArgumentException("Feature cannot be null or empty");
        }
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
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        FeatureMetadata that = (FeatureMetadata) o;

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
