package com.github.cloudyrock.dimmer;

public class FeatureInvocation {
    private final String feature;
    private final String methodName;
    private final Class declaringType;
    private final Object[] args;

    public FeatureInvocation(String feature, String methodName, Class declaringType, Object[] args) {
        this.feature = feature;
        this.methodName = methodName;
        this.declaringType = declaringType;
        this.args = args;

    }

    public String getFeature() {
        return feature;
    }

    public String getMethodName() {
        return methodName;
    }

    public Class getDeclaringType() {
        return declaringType;
    }

    public Object[] getArgs() {
        return args;
    }

}
