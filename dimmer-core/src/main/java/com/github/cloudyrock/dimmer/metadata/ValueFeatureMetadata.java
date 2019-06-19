package com.github.cloudyrock.dimmer.metadata;

public final class ValueFeatureMetadata extends FeatureMetadata {

    private final Object valueToReturn;

    public ValueFeatureMetadata(String feature, String operation, Object valueToReturn) {
        super(feature, operation);
        this.valueToReturn = valueToReturn;
    }

    public Object getValueToReturn() {
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
