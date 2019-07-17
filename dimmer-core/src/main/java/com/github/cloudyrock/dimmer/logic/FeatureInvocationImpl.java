package com.github.cloudyrock.dimmer.logic;

import com.github.cloudyrock.dimmer.FeatureInvocation;

/**
 * Represents method call with all its information.
 */
class FeatureInvocationImpl implements FeatureInvocation {
    /**
     * Name of the feature covering the set of methods that define an entire functionality.
     */
    private final String feature;
    /**
     * Name of the operation for the annotated method. This operation should be unique
     * within a feature bound
     */
    private final String operation;

    /**
     * Invoked method's name
     */
    private final String methodName;

    /**
     * Owner class of the method
     */
    private final Class declaringType;

    /**
     * Returning type of the method
     */
    private final Class returnType;

    /**
     * The arguments which the method was invoked with
     */
    private final Object[] args;

    FeatureInvocationImpl(String feature, String operation, String methodName, Class declaringType, Object[] args, Class returnType) {
        this.feature = feature;
        this.operation = operation;
        this.methodName = methodName;
        this.declaringType = declaringType;
        this.args = args;
        this.returnType = returnType;
    }

    @Override
    public String getFeature() {
        return feature;
    }

    @Override
    public String getOperation() {
        return operation;
    }

    @Override
    public String getMethodName() {
        return methodName;
    }

    @Override
    public Class getDeclaringType() {
        return declaringType;
    }

    @Override
    public Object[] getArgs() {
        return args;
    }

    @Override
    public Class getReturnType() {
        return returnType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        FeatureInvocationImpl that = (FeatureInvocationImpl) o;

        if (!feature.equals(that.feature)
                || !operation.equals(that.operation)
                || !methodName.equals(that.methodName)
                || !declaringType.equals(that.declaringType)) {
            return false;
        }
        return returnType.equals(that.returnType);
    }

    @Override
    public int hashCode() {
        int result = feature.hashCode();
        result = 31 * result + operation.hashCode();
        result = 31 * result + methodName.hashCode();
        result = 31 * result + declaringType.hashCode();
        result = 31 * result + returnType.hashCode();
        return result;
    }
}
