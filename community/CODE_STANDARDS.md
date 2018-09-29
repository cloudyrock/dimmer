
# Dimmer Code Standard

## Introduction
--------------------------------------------------------------------------------------------------------------------------------

We used [Google Java Style Guide][google-tyle-url] as standard foundation
with our own extensions, which you can find in this document.


## General
--------------------------------------------------------------------------------------------------------------------------------

### Column limit: 120

We add extra 20 characters to the goggle standard column limit: 120 characters column limit.

This is ok for most of screens and allow developers to split the screen in two, actual class and test.

### Horizontal alignment: never required

**Terminology Note:** _Horizontal alignment_ is the practice of adding a variable number of additional spaces in your code with the goal of making certain tokens appear directly below certain other tokens on previous lines.

This practice is NOT permitted by Dimmer Style.

Here is an example without alignment, then using alignment:

private  int x;  // this is OK  private  Color color;  // this OK too  private  int x;  // NOT allowed  private  Color color;

### Access modifier
In this section we want to encourage the right modifier in every case.

* __package-private__: The default modifier for classes and methods.
* __private__: Default modifier for attributes and for methods accessed only but the current class.
* __protected__: For members which are(or could be) accessed by children classes in a different package.
* __public__: Only for classes, methods and static constants that are(or could be) accessed publicly.


## Programming Practices
--------------------------------------------------------------------------------------------------------------------------------------------------

### finals

You should be using `final` modifier for classes and members are instructed in the Java standard guide. In  this section we add specific scenarios where
you should be using `final` too.

* __Utility classes__: In utility classes that are accessed statically.
* __Local variables__: As general rule, use final in variables where reference is not intended to be modified. This helps readability.
* __Method parameter__: Not required, but permitted, applying the rule for 'Local variables'

### Immutability

As part of this project we highly encourage immutability, so please apply it wherever is possible.

### Optional and nulls

Avoid nulls as much as possible. Use `Optional` wherever a method could return a null instance.

### Generic

As part of this project we like to use meaningful type parameter names, although is opposite to the java standard. So this is not required, but appreciated.

Example:

```java
public interface GenericInterface<GENERIC_PARAMETER> {
...
}
```


### Java 8 API

Use the Java 8 API where possible.


## Test
--------------------------------------------------------------------------------------------------------------------------------------------------

### Test file Naming

* __Unit tests__: Unit test files must be named using the pattern *UTest.java
* __Integration tests__: Integration test files must be named using the pattern *ITest.java

### Method naming

Use the **should**XXX_**when**XXX_**if**XXXXX pattern for test names.

Example:
```java
public shouldReturn4_whenSum_ifParametersAre2And2() {
...
}

```

### Coverage

Any code delivered must be covered by unit and integrations test. We ensure this by using [Jacoco plugin](https://www.eclemma.org/jacoco/).

Coverage for unit and integrations tests are checked separately(both have __80% threshold coverage__)

You can run the check simply by running `mvn clean verify`

**Note:** Please notice that when we you run jacoco(by mvn), it checks the entire project. However, the first thing we do when we perform a review
is to run sonar which check that just the new code fits the coverage threshold. This means that when you verify locally you coverage, it passes, but it
doesn't when we review. For this we suggest you make sure your new code fits the coverage threshold.


## Javadoc
----------------------------------------------------------------------------------------------------------------------

As part of this project we encourage self-explanatory code, so Javadoc is only required for public members which will be (or could be) used by the users of this library.

Where Javadoc required, we apply the [Google style](http://google.github.io/styleguide/javaguide.html#s7-javadoc)





[google-style-url]:http://google.github.io/styleguide/javaguide.html