# Dimmer: Lightweight Java library for flexible feature toggling
Dimmer is a lightweight Java library to manage feature toggling. Unlike others Dimmer does not work 
just in switch mode, black or white. It provides a flexible way to respond to disabled
features by adding customizable behaviours and pre-configured responses.

## Why Dimmer?
As developer we all have found that toggling off features in some stages of the project 
is needed. Sometimes is not about to temporally toggle off some features, but providing 
some mocked for specific environments, for example when a 3rd party remote service is not
available.

Most of teams just use a home-made solution which normally implies the use of if-else 
in production code, which for obvious reasons is not ideal(production code should contain
just production code ;) ). Some other teams use some 3rd parties, but most of them are
either too big and complex, or too simple and don't provide the flexibility to provide
different behaviours depending on different scenarios.

## How does Dimmer work?
Dimmer works by processing annotated methods, containing the feature which is toggled off.
This feature is configured with a builder, which is the responsible of providing the 
mocked behaviour in a vey simple way.

To approach this, Dimmer is based on aspects.

## How it looks?
```java
public class Main {

    private static final String FEATURE_NAME = "feature_name";

    public static void main(String... args) {
        DimmerBuilder
                .local()
                .defaultEnvironment()
                .featureWithValue(FEATURE_NAME, "fake value")
                .buildWithDefaultEnvironment();

        final String result = new Main().runFeaturedMethod();
        System.out.println(result); //It prints 'fake value'
    }

    @DimmerFeature(FEATURE_NAME)
    private String runFeaturedMethod() {
        return "real value";
    }

}
```

## Basic configurations
### Throwing a default exception 
The most basic scenario is when we just throw a default exception when a feature is called.
```java
        DimmerBuilder
                .local()
                .defaultEnvironment()
                .featureWithDefaultException(FEATURE_NAME)
                .buildWithDefaultEnvironment();
```

This will throw a DimmerExecutionException(we'll explain how to thrown your own exception) 
when a method annotated with FEATURE_NAME is invoked.

### Returning a fixed value
As you could see in 'How does it work?', the example is using 'featureWithValue'. What this does
is just return the value provided in the configuration every time a annotated method, with the given feature,
is called. Please notice that the object can be an instance of a custom class, however you should
ensure that it matches whatever the annotated method returns, otherwise it will throw a DimmerConfigurationException, 
for casting error. In this context, 'match' does not mean is the same class, it could the method returns an interface 
and you provide an implementation or it's a parent class and a child class is provided.
```java
        DimmerBuilder
                .local()
                .defaultEnvironment()
                .featureWithValue(FEATURE_NAME, "fake value")
                .buildWithDefaultEnvironment();
```

### When exceptions and fixed values are not enough: Behaviours
Sometimes throwing an exception or returning a fixed value is not flexible enough. You need to return a dynamic value 
or in some scenarios you want to return a value, while  in others you want to throw an exception. It's fine, dimmer gives you 
all the flexibility you need with what we call 'behaviours'. 

Before providing some sample,lets clarify some concepts first.
- FeatureInvocation: It's an object which encapsulates the information regarding the invocation(signature, arguments, etc.). 
Its structure  looks like this:
```java
    /** Feature covering invoked method */
    private final String feature;
    /** Invoked method's name */
    private final String methodName;
    /** Owner class of the method */
    private final Class declaringType;
    /** Returning type of the method */
    private final Class returnType;
    /** The arguments which the method was invoked with */
    private final Object[] args;
```

- Behaviour: As its name suggests, provides the capability to perform some dynamic actions for a given feature.
In Dimmer, a behaviour is Java 8 Function, which takes as input parameter an FeatureInvocation
```java
public class Main {

    private static final String FEATURE_NAME = "feature_name";

    public static void main(String... args) {

        final Function<FeatureInvocation, BigDecimal> behaviour =
                featureInvocation -> {
                    final Integer input = (Integer)featureInvocation.getArgs()[0];
                    if(input == null) {
                        throw new IllegalArgumentException("Input cannot be null");
                    }
                    return new BigDecimal(input);
                };

        DimmerBuilder
                .local()
                .defaultEnvironment()
                .featureWithBehaviour(FEATURE_NAME, behaviour)
                .buildWithDefaultEnvironment();

        final BigDecimal result = new Main().runFeaturedMethod(100);
        System.out.println(result);//It prints 100 as BigDecimal


        new Main().runFeaturedMethod(null);// Throws exception
    }

    @DimmerFeature(FEATURE_NAME)
    private BigDecimal runFeaturedMethod(Integer intValue) {
        return null;
    }

}
```

## Throwing custom exceptions
We have seen how to throw a default exception(DimmerExecutionException), but sometimes you
prefer to return your own exception. That's still possible with Dimmer, however you exception needs to fulfill 
at least one of the following requirements, but it will be always a unchecked exception type(RuntimeException):
- Provides a constructor wich takes a FeatureInvocation as parameter
- Provides a default constructor with no parameters

In case that the class provides both constructors, Dimmer will prioritize the one that takes a FeatureInvocation as parameter 
over the default constructor.
```java
public class Main {

    private static final String FEATURE_NAME = "feature_name";

    public static class MyException extends RuntimeException {

        private final String feature;
        private final String methodName;

        public MyException(FeatureInvocation featureInvocation) {
            this.feature = featureInvocation.getFeature();
            this.methodName = featureInvocation.getMethodName();
        }

        @Override
        public String getMessage() {
            return String.format("Feature %s called via method %s", feature, methodName);
        }
    }

    public static void main(String... args) {
        DimmerBuilder
                .local()
                .defaultEnvironment()
                .featureWithException(FEATURE_NAME, MyException.class)
                .buildWithDefaultEnvironment();

        new Main().runFeaturedMethod();
        //It will throw exception and print message 'Feature feature_name called via method runFeaturedMethod'
    }

    @DimmerFeature(FEATURE_NAME)
    private String runFeaturedMethod() {
        return "real value";
    }

}
```


