package net.cloudyrock.toggler;

import org.junit.Test;

import static org.junit.Assert.assertNull;

public class ToggleOffAndNullITest {

    @Test()
    public void toggleOffAndThrow_default() {
        assertNull(new DummyToggleOffAndNullClass().returnNullAnnotated());
    }

    class DummyToggleOffAndNullClass {

        @ToggleOffAndNull
        String returnNullAnnotated() {
            return "REAL CALL";
        }


    }
}
