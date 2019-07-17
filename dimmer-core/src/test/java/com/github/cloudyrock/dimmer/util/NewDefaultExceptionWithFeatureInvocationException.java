package com.github.cloudyrock.dimmer.util;

import com.github.cloudyrock.dimmer.FeatureInvocation;

public class NewDefaultExceptionWithFeatureInvocationException extends RuntimeException {

    private final FeatureInvocation featureInvocation;

    public NewDefaultExceptionWithFeatureInvocationException(FeatureInvocation featureInvocation) {
        super();
        this.featureInvocation = featureInvocation;

    }

    public FeatureInvocation getFeatureInvocation() {
        return featureInvocation;
    }

}
