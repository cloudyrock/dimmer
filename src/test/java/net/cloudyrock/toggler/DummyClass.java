package net.cloudyrock.toggler;

public class DummyClass {

    @ToggleOff
    public void annotatedMethod() {
        System.out.println("method annotated with OFF");
    }

    public void notAnnotatedMethod() {
        System.out.println("Not annotated method");
    }

    @ToggleCheck(feature = "CALL_METHOD")
    public void annotatedMethod2() {
        System.out.println("method annotated with CHECK");
    }

}
