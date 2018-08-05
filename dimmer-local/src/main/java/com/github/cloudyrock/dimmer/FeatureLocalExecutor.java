package com.github.cloudyrock.dimmer;

import static org.slf4j.event.Level.INFO;
import static org.slf4j.event.Level.TRACE;

public class FeatureLocalExecutor extends FeatureProcessorBase
        implements FeatureExecutor {

    FeatureLocalExecutor(DimmerSlf4j logger) {
        super(logger);
    }

    FeatureLocalExecutor() {
        this(DimmerSlf4j.nullLogger());
    }

    @Override
    public Object executeDimmerFeature(String feature,
                                       FeatureInvocation featureInvocation,
                                       MethodCaller realMethod) throws Throwable {
        if (isFeatureConfigured(feature)) {
            logDimmerInterception(feature, featureInvocation);
            return runFeature(feature, featureInvocation);
        } else {
            logger.logWithPrefix(
                    TRACE,
                    "Dimmer ignored due to feature {} is not configured", feature);
            return realMethod.call();
        }
    }

    private void logDimmerInterception(String feature,
                                       FeatureInvocation featureInvocation) {
        logger.logWithPrefix(
                INFO,
                "Intercepted method {}.{}() for feature {}",
                featureInvocation.getDeclaringType(),
                featureInvocation.getMethodName(),
                feature);
    }

}
