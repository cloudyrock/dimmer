package com.github.cloudyrock.dimmer;

public class FeatureLocalExecutor extends FeatureProcessorBase
        implements FeatureExecutor {

    protected static final DimmerSlf4j logger =
            new DimmerSlf4j(FeatureLocalBuilder.class);

    @Override
    public Object executeDimmerFeature(String feature,
                                       FeatureInvocation featureInvocation,
                                       MethodCaller realMethod) throws Throwable {
        if (isFeatureConfigured(feature)) {
            logDimmerInterception(feature, featureInvocation);
            return runFeature(feature, featureInvocation);
        } else {
            logger.trace("Dimmer ignored due to feature {} is not configured", feature);
            return realMethod.call();
        }
    }

    private void logDimmerInterception(String feature,
                                       FeatureInvocation featureInvocation) {
        logger.info(
                "Intercepted method {}.{}() for feature {}",
                featureInvocation.getDeclaringType(),
                featureInvocation.getMethodName(),
                feature);
    }

}
