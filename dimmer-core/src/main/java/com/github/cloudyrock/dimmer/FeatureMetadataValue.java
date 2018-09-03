package com.github.cloudyrock.dimmer;

public final class FeatureMetadataValue extends FeatureMetadata {

    private final Object valueToReturn;

    public FeatureMetadataValue(String feature, String operation, Object valueToReturn) {
        super(feature, operation);
        this.valueToReturn = valueToReturn;
    }

    public Object getValueToReturn() {
        return valueToReturn;
    }
}
