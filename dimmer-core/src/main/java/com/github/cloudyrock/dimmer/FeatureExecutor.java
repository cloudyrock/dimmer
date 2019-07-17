package com.github.cloudyrock.dimmer;


/**
 * Interface to define the API to run a Dimmer feature.
 */
public interface FeatureExecutor {

    /**
     * Executes a feature matching the given information with the behaviour injected
     * by a builder.
     * @param feature Feature covering the execution
     * @param operation Operation, which combined with feature, will provide the behaviour stored.
     * @param featureInvocation Description of the method's invocation. See {@link FeatureInvocation}
     * @param realMethod Object which wraps the real method's invocation. See {@link MethodCaller}
     * @return An instance o the object configured in the builder if the feature is disabled,
     * or the returning object from the realMethod's call otherwise.
     * @throws Throwable if a unexpected, non configured, exception occurs.
     *
     * @see FeatureInvocation
     * @see MethodCaller
     */
    Object executeDimmerFeature(String feature, String operation, FeatureInvocation featureInvocation, MethodCaller realMethod) throws Throwable;
}
