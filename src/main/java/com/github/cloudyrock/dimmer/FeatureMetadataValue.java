package com.github.cloudyrock.dimmer;

public final class FeatureMetadataValue extends FeatureMetadata {

    private final Object valueToReturn;

    public FeatureMetadataValue(String feature, Object valueToReturn) {
        super(feature);
        this.valueToReturn = valueToReturn;
    }

    public Object getException() {
        return valueToReturn;
    }
}
