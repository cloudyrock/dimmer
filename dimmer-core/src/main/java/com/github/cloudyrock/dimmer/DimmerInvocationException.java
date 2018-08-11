package com.github.cloudyrock.dimmer;

import com.github.cloudyrock.dimmer.FeatureInvocation;

/**
 * Default exception that will be thrown if no other is configured.
 * It's thrown at runtime represents a desired behaviour of throwing an exception.
 *
 * @author Antonio Perez Dieppa
 * @since 11/06/2018
 */
public class DimmerInvocationException extends RuntimeException {

    private static final long serialVersionUID = 159403250595728227L;
    private final FeatureInvocation invocationInfo;

    public DimmerInvocationException(FeatureInvocation featureInvocation) {
        this.invocationInfo = featureInvocation;
    }

    public FeatureInvocation getInvocationInfo() {
        return invocationInfo;
    }

    @Override
    public String getMessage() {
        return String.format(">>> Feature %s not available for %s.%s(...)",
                invocationInfo.getFeature(),
                invocationInfo.getDeclaringType().getCanonicalName(),
                invocationInfo.getMethodName());
    }

}
