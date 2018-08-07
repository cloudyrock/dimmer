package com.github.cloudyrock.dimmer;

/**
 * Represents method call with all its information.
 *
 * @author Antonio Perez Dieppa
 * @since 11/06/2018
 */
public class FeatureInvocationBase {
    /**
     * Method's name
     */
    private final String feature;

    /**
     * The arguments which the method was invoked with
     */
    private final Object[] args;

    public FeatureInvocationBase(String feature, Object[] args) {
        this.feature = feature;
        this.args = args;
    }

    public String getFeature() {
        return feature;
    }

    public Object[] getArgs() {
        return args;
    }
}
