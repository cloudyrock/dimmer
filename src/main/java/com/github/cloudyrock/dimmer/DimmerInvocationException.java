package com.github.cloudyrock.dimmer;

import static com.github.cloudyrock.dimmer.DimmerFeature.ALWAYS_OFF;

public class DimmerInvocationException extends RuntimeException {

    private final FeatureInvocation invocationInfo;

    public DimmerInvocationException(FeatureInvocation featureInvocation) {
        this.invocationInfo = featureInvocation;
    }

    public FeatureInvocation getInvocationInfo() {
        return invocationInfo;
    }

    @Override
    public String getMessage() {
        if (ALWAYS_OFF.equals(invocationInfo.getFeature())) {
            return String.format(">>> %s.%s(...) is ALWAYS_OFFf",
                    invocationInfo.getDeclaringType().getCanonicalName(),
                    invocationInfo.getMethodName());
        } else {
            return String.format(">>> Feature %s not available for %s.%s(...)",
                    invocationInfo.getFeature(),
                    invocationInfo.getDeclaringType().getCanonicalName(),
                    invocationInfo.getMethodName());
        }
    }

}
