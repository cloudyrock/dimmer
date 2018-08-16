package com.github.cloudyrock.dimmer;

import java.util.HashSet;
import java.util.Set;

public class FeatureExecutorImpl extends FeatureProcessorBase
        implements FeatureExecutor {

    private static final DimmerLogger logger =
            new DimmerLogger(FeatureConfigurationBuilder.class);

    FeatureExecutorImpl() {
        this(new HashSet<>(), DimmerInvocationException.class);
    }

    FeatureExecutorImpl(Set<FeatureMetadata> featureMetadataSet,
                        Class<? extends RuntimeException> defaultException) {
        super(featureMetadataSet, defaultException);
    }

    @Override
    public Object executeDimmerFeature(String feature,
                                       String operation,
                                       FeatureInvocation featureInvocation,
                                       MethodCaller realMethod) throws Throwable {
        if (isFeatureEnabled(feature, operation)) {
            logger.trace("Dimmer ignored due to feature {} is not configured", feature);
            return realMethod.call();
        } else {
            logDimmerInterception(feature, operation,  featureInvocation);
            return runFeature(feature, operation, featureInvocation);
        }
    }

    private void logDimmerInterception(String feature,
                                       String operation,
                                       FeatureInvocation featureInvocation) {
        logger.info(
                "Intercepted method {}.{}() for feature {} and operation {}",
                featureInvocation.getDeclaringType(),
                featureInvocation.getMethodName(),
                feature,
                operation);
    }

}
