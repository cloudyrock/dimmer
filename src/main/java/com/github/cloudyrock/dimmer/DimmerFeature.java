package com.github.cloudyrock.dimmer;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


/**
 * Annotation containing the feature configuration for a method.
 *
 * @author Antonio Perez Dieppa
 * @see DimmerProcessor
 * @see FeatureInvocation
 * @see DimmerInvocationException
 * @see DimmerBehaviour
 *
 * @author Antonio Perez Dieppa
 * @since 11/06/2018
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface DimmerFeature {

    /**
     * Indicates the feature, configured via (@{@link DimmerProcessor}), that describes
     * the behaviour instead of invoking the real method, or ALWAYS_OFF,
     * which means that is not based in any feature configuration, it's always switched off
     * (unless runRealMethod is called)
     *
     * @see DimmerProcessor
     * @return feature or ALWAYS_OFF
     */
    String value();

    /**
     * Mechanism to overwrite the default behaviour. The possibles values are explained
     * in (@{@link DimmerBehaviour}).
     *
     * @see DimmerBehaviour
     * @return overwritten behaviour
     */
    DimmerBehaviour behaviour() default DimmerBehaviour.DEFAULT;

    /**
     * Indicates which exception will be thrown when the annotated method is invoked.
     * If none exception is given, it will throw the default exception configured with
     * (@{@link FeatureInvocation}) or (@{@link DimmerInvocationException}) if
     * no default exception has been configured
     * Ignored if behaviour is other than THROW_EXCEPTION
     *
     * Notice the exception must have either an empty constructor or a contractor with only
     * one parameter, (@{@link FeatureInvocation})
     *
     * @see DimmerProcessor
     * @see FeatureInvocation
     * @return exception to be thrown when the annotated method is invoked.
     */
    Class<? extends RuntimeException> exception() default NULL_EXCEPTION.class;

    /**
     * If true, dimmer is disabled an will invoke the real method with the real parameters.
     * @return if the real method will be invoked
     */
    boolean runRealMethod() default false;

    /**
     * Flag that indicates the annotated method will be always switched off,
     * unless runRealMethod is true, in which case
     */
    String ALWAYS_OFF = "ALWAYS_OFF";

    /**
     * Used to overwrite the default behaviour.
     * DEFAULT: Will execute the default behaviour based on the feature configuration or
     *          ALWAYS_OFF default behaviour.
     *
     * THROW_EXCEPTION: Overwrite the default behaviour with exception. If parameter
     *          exception in (@{@link DimmerFeature}) is not passed, will thrown
     *          the default exception or (@{@link DimmerInvocationException})
     *
     * RETURN_NULL: Overwrite the default behaviour with null.
     *
     * Al these values will no have any effect if parameter runRealMethod is true.
     */
    enum DimmerBehaviour {DEFAULT, THROW_EXCEPTION, RETURN_NULL}

    class NULL_EXCEPTION extends RuntimeException {
    }
}
