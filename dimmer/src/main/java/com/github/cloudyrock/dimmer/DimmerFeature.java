package com.github.cloudyrock.dimmer;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Method annotation to indicates the method is suitable for Dimmer to intercept and
 * run the configuration applied in the builder
 *
 * @since 11/06/2018
 */
@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface DimmerFeature {

    /**
     * Name of the feature for a a global functionality covering a set of methods.
     * This feature can be shared for a set of methods that conforms a functionality.
     * @return Name of the feature
     */
    String value();

    /**
     * Name of the operation for the annotated method. This operation shouldn't shared
     * among other methods, as it's used to configure the specific method's behaviour.
     * <p/>
     * It must be unique within a feature
     * @return Name of the operation
     */
    String op();

}
