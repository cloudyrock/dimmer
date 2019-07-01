package com.github.cloudyrock.dimmer.metadata;

import com.github.cloudyrock.dimmer.FeatureInvocation;

import java.util.function.Function;

public final class ValueFeatureMetadata extends FeatureMetadata {

    private final Object valueToReturn;

    public ValueFeatureMetadata(String feature, String operation, Object valueToReturn) {
        super(feature, operation);
        this.valueToReturn = valueToReturn;
    }

    public Object getValueToReturn() {
        return valueToReturn;
    }


    public Function<FeatureInvocation, Object> getFunction() {
        return featureInvocation -> valueToReturn;
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
