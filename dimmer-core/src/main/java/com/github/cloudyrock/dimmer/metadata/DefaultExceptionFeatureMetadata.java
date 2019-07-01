package com.github.cloudyrock.dimmer.metadata;

import com.github.cloudyrock.dimmer.FeatureInvocation;
import com.github.cloudyrock.dimmer.exception.DimmerInvocationException;

import java.util.function.Function;

public final class DefaultExceptionFeatureMetadata extends FeatureMetadata {


    public DefaultExceptionFeatureMetadata(String feature, String operation) {
        super(feature, operation);
    }


    public Function<FeatureInvocation, Object> getFunction() {
        return featureInv -> ExceptionUtil.throwException(DimmerInvocationException.class, featureInv);
    }

}
