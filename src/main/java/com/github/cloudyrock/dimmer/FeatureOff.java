package com.github.cloudyrock.dimmer;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static com.github.cloudyrock.dimmer.FeatureOffBehaviour.THROW_EXCEPTION;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface FeatureOff {

    FeatureOffBehaviour value() default THROW_EXCEPTION;

    //exception must have an empty constructor
    Class<? extends RuntimeException> exception() default DimmerInvocationException.class;

}
