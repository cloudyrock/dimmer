package com.github.cloudyrock.dimmer;

import com.github.cloudyrock.dimmer.FeatureInvocation;

public class ExceptionWithFeatureInvocation extends RuntimeException{

    private final FeatureInvocation featureInvocation;

    public ExceptionWithFeatureInvocation(FeatureInvocation featureInvocation) {
        this.featureInvocation = featureInvocation;
    }

    public FeatureInvocation getFeatureInvocation() {
        return featureInvocation;
    }
}
