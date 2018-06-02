package net.cloudyrock.toggler;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface ToggleOffAndThrow {
    Class<? extends RuntimeException> value() default TogglerInvocationException.class;

}
