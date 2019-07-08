package com.github.cloudyrock.dimmer;


import com.github.cloudyrock.dimmer.logic.DimmerAspect;

/**
 * Interface representing a real method invocation.
 * By using this interface the method interceptor({@link DimmerAspect} or any other) is able
 * to call the real invoked method if the feature is enabled.
 */
@FunctionalInterface
public interface MethodCaller {

    /**
     * It executes the real method
     * @return The object returned by the real method
     * @throws Throwable if a unexpected, non configured, exception occurs.
     */
    Object call() throws Throwable;
}
