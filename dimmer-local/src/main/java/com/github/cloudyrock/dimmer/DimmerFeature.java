package com.github.cloudyrock.dimmer;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation containing the feature configuration for a method.
 *
 * @author Antonio Perez Dieppa
 * @see FeatureLocalExecutor
 * @see FeatureInvocation
 * @see DimmerInvocationException
 *
 * @author Antonio Perez Dieppa
 * @since 11/06/2018
 */
@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface DimmerFeature {

    /**
     * Indicates the feature, configured via (@{@link FeatureLocalExecutor}), that describes
     * the behaviour instead of invoking the real method
     *
     * @see FeatureLocalExecutor
     * @return feature
     */
    String value();

}
