package net.cloudyrock.toggler;

import org.junit.Test;

public class ToggleOffAndThrowITest {

    @Test(expected = TogglerInvocationException.class)
    public void toggleOffAndThrow_default() {
        new DummyToggleOffAndThrowClass().throwDefaultException();
    }

    @Test(expected = DummyException.class)
    public void toggleOffAndThrow_custom_exception() {
        new DummyToggleOffAndThrowClass().throwCustomException();
    }

    class DummyToggleOffAndThrowClass {

        @ToggleOffAndThrow
        String throwDefaultException() {
            return "REAL CALL";
        }

        @ToggleOffAndThrow(DummyException.class)
        String throwCustomException() {
            return "REAL CALL";
        }

    }
}
