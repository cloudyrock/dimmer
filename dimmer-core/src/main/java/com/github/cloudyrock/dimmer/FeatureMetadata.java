package com.github.cloudyrock.dimmer;

abstract class FeatureMetadata {

    private final String feature;

    protected FeatureMetadata(String feature) {
        if (feature == null || feature.isEmpty()) {
            throw new IllegalArgumentException("Feature cannot be null or empty");
        }
        this.feature = feature;
    }

    public String getFeature() {
        return feature;
    }

    @Override
    public boolean equals(Object o) {
        return this == o
                || o != null && getClass() == o.getClass()
                && feature.equals(((FeatureMetadata) o).feature);

    }

    @Override
    public int hashCode() {
        return feature.hashCode();
    }
}
