package com.github.cloudyrock.dimmer;

public interface FeatureInvocation {
    String getFeature();

    String getOperation();

    String getMethodName();

    Class getDeclaringType();

    Object[] getArgs();

    Class getReturnType();
}
