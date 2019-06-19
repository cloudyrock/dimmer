package com.github.cloudyrock.dimmer;


public class DummyExceptionWithFeatureInvocation extends RuntimeException{

    private final FeatureInvocation featureInvocation;

    public DummyExceptionWithFeatureInvocation(FeatureInvocation featureInvocation) {
        this.featureInvocation = featureInvocation;
    }

    public FeatureInvocation getFeatureInvocation() {
        return featureInvocation;
    }
}
