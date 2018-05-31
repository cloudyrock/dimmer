package net.cloudyrock.toggler;

import org.junit.Test;

public class YourAspectTest {


    @Test
    public void runningAspect() {
        DummyClass d = new DummyClass();
        d.annotatedMethod();
        System.out.println("\n\n\n\n\n");
        d.notAnnotatedMethod();

    }
}
