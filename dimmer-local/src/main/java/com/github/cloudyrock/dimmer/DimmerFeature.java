package com.github.cloudyrock.dimmer;

import com.github.cloudyrock.dimmer.exceptions.DimmerInvocationException;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation containing the feature configuration for a method.
 *
 * @author Antonio Perez Dieppa
 * @see FeatureLocalProcessor
 * @see FeatureInvocation
 * @see DimmerInvocationException
 *
 * @author Antonio Perez Dieppa
 * @since 11/06/2018
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface DimmerFeature {

    /**
     * Indicates the feature, configured via (@{@link FeatureLocalProcessor}), that describes
     * the behaviour instead of invoking the real method
     *
     * @see FeatureLocalProcessor
     * @return feature
     */
    String value();

}
