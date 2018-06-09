package com.github.cloudyrock.dimmer;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface DimmerFeature {

    String value();

    DimmerBehaviour behaviour() default DimmerBehaviour.DEFAULT;

    Class<? extends RuntimeException> exception() default NULL_EXCEPTION.class;


    boolean disabled() default false;

    String ALWAYS_OFF = "ALWAYS_OFF";

    enum DimmerBehaviour {DEFAULT, THROW_EXCEPTION, RETURN_NULL}

    class NULL_EXCEPTION extends RuntimeException {
    }
}
