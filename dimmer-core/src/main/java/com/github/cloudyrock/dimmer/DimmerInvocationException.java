package com.github.cloudyrock.dimmer;

/**
 * Default exception that will be thrown if no other is configured.
 * If thrown at runtime represents a desired behaviour of throwing an exception.
 */
public class DimmerInvocationException extends RuntimeException {

    private static final long serialVersionUID = 159403250595728227L;
    /**
     * Feature invocation to describe the real call to the intercepted method
     */
    private final FeatureInvocation invocationInfo;

    /**
     * Constructor with a featureInvocation as parameter
     * @param featureInvocation real method's invocation metadata
     */
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
