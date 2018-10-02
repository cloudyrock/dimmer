package com.github.cloudyrock.dimmer;

import java.util.Objects;

final class ValueFeatureMetadata extends FeatureMetadata {

    private final Object valueToReturn;

    ValueFeatureMetadata(String feature, String operation, Object valueToReturn) {
        super(feature, operation);
        this.valueToReturn = valueToReturn;
    }

    Object getValueToReturn() {
        return valueToReturn;
    }


    @Override
    public boolean equals(Object o) {
        return super.equals(o);
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }
}
