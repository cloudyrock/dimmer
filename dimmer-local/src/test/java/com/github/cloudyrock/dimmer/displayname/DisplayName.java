package com.github.cloudyrock.dimmer.displayname;

import org.junit.rules.ExpectedException;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * If using (@{@link ExpectedException}) it needs to be non-final and non-static
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface DisplayName {

    String value();
}
