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
     * The arguments which the method was invoked with
     */
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
