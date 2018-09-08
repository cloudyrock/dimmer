package com.github.cloudyrock.dimmer;

/**
 * Represents method call with all its information.
 *
 * @since 11/06/2018
 */
public class FeatureInvocation {
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

    FeatureInvocation(String feature,
                             String operation,
                             String methodName,
                             Class declaringType,
                             Object[] args,
                             Class returnType) {
        this.feature = feature;
        this.operation = operation;
        this.methodName = methodName;
        this.declaringType = declaringType;
        this.args = args;
        this.returnType = returnType;
    }

    public String getFeature() {
        return feature;
    }

    public String getOperation() {
        return operation;
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

    public Class getReturnType() {
        return returnType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        FeatureInvocation that = (FeatureInvocation) o;

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
