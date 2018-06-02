package net.cloudyrock.toggler;

public class TogglerConfigurationException extends RuntimeException{
    public TogglerConfigurationException(ReflectiveOperationException e) {
        super(e);
    }
}
