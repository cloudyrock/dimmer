package com.github.cloudyrock.dimmer.exceptions;

import com.github.cloudyrock.dimmer.FeatureInvocation;

public class DummyExceptionWithFeatureInvocation extends RuntimeException{

    private final FeatureInvocation featureInvocation;

    public DummyExceptionWithFeatureInvocation(FeatureInvocation featureInvocation) {
        this.featureInvocation = featureInvocation;
    }

    public FeatureInvocation getFeatureInvocation() {
        return featureInvocation;
    }
}
