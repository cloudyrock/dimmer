package com.github.cloudyrock.dimmer.exceptions;

import com.github.cloudyrock.dimmer.FeatureInvocationBase;

public class DummyExceptionWithFeatureInvocation extends RuntimeException{

    private final FeatureInvocationBase featureInvocation;

    public DummyExceptionWithFeatureInvocation(FeatureInvocationBase featureInvocation) {
        this.featureInvocation = featureInvocation;
    }

    public FeatureInvocationBase getFeatureInvocation() {
        return featureInvocation;
    }
}
