package com.github.cloudyrock.dimmer;

import java.util.Arrays;

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
    private final Class returnType;

    /**
     * The arguments which the method was invoked with
     */
    private final Object[] args;

    public FeatureInvocation(String feature,
                             String methodName,
                             Class declaringType,
                             Object[] args,
                             Class returnType) {
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

    public Class getReturnType() {
        return returnType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        final FeatureInvocation that = (FeatureInvocation) o;

        if (!feature.equals(that.feature)) {
            return false;
        }
        if (!methodName.equals(that.methodName)) {
            return false;
        }
        if (!declaringType.equals(that.declaringType)) {
            return false;
        }
        // Probably incorrect - comparing Object[] arrays with Arrays.equals
        return Arrays.equals(args, that.args);
    }

    @Override
    public int hashCode() {
        int result = feature.hashCode();
        result = 31 * result + methodName.hashCode();
        result = 31 * result + declaringType.hashCode();
        result = 31 * result + Arrays.hashCode(args);
        return result;
    }
}
