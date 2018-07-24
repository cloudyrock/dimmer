package com.github.cloudyrock.dimmer;

import org.aspectj.lang.ProceedingJoinPoint;

interface FeatureExecutor {

    Object executeDimmerFeature(
            String feature,
            FeatureInvocation featureInvocation,
            ProceedingJoinPoint realMethod) throws Throwable;
}
