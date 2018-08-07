package com.github.cloudyrock.dimmer.exceptions;

import com.github.cloudyrock.dimmer.FeatureInvocationBase;

/**
 * Default exception that will be thrown if no other is configured.
 * It's thrown at runtime represents a desired behaviour of throwing an exception.
 *
 * @author Antonio Perez Dieppa
 * @since 11/06/2018
 */
public class DimmerInvocationException extends RuntimeException {

    private static final long serialVersionUID = 159403250595728227L;
    private final FeatureInvocationBase invocationInfo;

    public DimmerInvocationException(FeatureInvocationBase featureInvocation) {
        this.invocationInfo = featureInvocation;
    }

    public FeatureInvocationBase getInvocationInfo() {
        return invocationInfo;
    }

    @Override
    public String getMessage() {
        return String.format(">>> Feature %s not available for invocation %s",
                invocationInfo.getFeature(), invocationInfo.toString());
    }

}
