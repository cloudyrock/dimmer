package net.cloudyrock.toggler;

public class DummyClass {

    @ToggledOFF
    public void annotatedMethod() {
        System.out.println("annotated method");
    }

    public void notAnnotatedMethod() {
        System.out.println("Not annotated method");
    }
}
