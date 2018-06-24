package com.github.cloudyrock.dimmer;

/**
 * Represents method call with all its information.
 *
 * @author Antonio Perez Dieppa
 * @since 11/06/2018
 */
public class FeatureInvocation {
    /**
     * Method's name
     */
    private final String feature;
    private final String methodName;

    /**
     * Owner class of the method
     */
    private final Class declaringType;

    /**
     * Returning type of the method
     */
    private final Object returnType;

    /**
     * The arguments which the method was invoked with
     */
    private final Object[] args;

    public FeatureInvocation(String feature, String methodName, Class declaringType, Object[] args, Object returnType) {
        this.feature = feature;
        this.methodName = methodName;
        this.declaringType = declaringType;
        this.args = args;
        this.returnType = returnType;
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

    public Object getReturnType() {
        return returnType;
    }
}
