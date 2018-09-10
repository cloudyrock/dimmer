package com.github.cloudyrock.dimmer;

final class FeatureMetadataValue extends FeatureMetadata {

    private final Object valueToReturn;

    FeatureMetadataValue(String feature, String operation, Object valueToReturn) {
        super(feature, operation);
        this.valueToReturn = valueToReturn;
    }

    Object getValueToReturn() {
        return valueToReturn;
    }
}
