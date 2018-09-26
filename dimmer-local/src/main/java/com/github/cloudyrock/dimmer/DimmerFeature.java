package com.github.cloudyrock.dimmer;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Method annotation to indicates the method is suitable for Dimmer to intercept and
 * run the configuration applied in the builder
 */
@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface DimmerFeature {

    /**
     * Name of the feature covering the set of methods that define an entire functionality.
     * This feature can be shared for a set of methods that conforms a functionality.
     * @return Name of the feature
     */
    String value();

    /**
     * Name of the operation for the annotated method. This operation should be unique
     * within a feature bound, as it's used to configure the specific method's behaviour.
     *
     * It must be unique within a feature
     * @return Name of the operation
     */
    String op();

}
