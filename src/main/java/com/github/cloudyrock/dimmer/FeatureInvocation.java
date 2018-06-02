package com.github.cloudyrock.dimmer;


public class FeatureInvocation {
    private final String methodName;
    private final Class declaringType;
    private final Object[] args;

    public FeatureInvocation(String methodName, Class declaringType, Object[] args) {
        this.methodName = methodName;
        this.declaringType = declaringType;
        this.args = args;

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
