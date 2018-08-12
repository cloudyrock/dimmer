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
Dimmer works by processing annotated methods(using aspects), containing the feature which is toggled off.
This feature is configured with a builder, which is the responsible of providing the 
mocked behaviour in a vey simple way.

In the next section you can see how it looks a basic configuration. Please notice that
terminal build methods(build and buildWithDefaultEnvironment) will inject the given configuration
to the aspect, which then will be ready, working and able to process the annotated features.



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

## Using Dimmer library


The first thing you need to know to use Dimmer is that it requires at least Java8.
Once you are sure you are using the right version of Java, you nee to import the Dimmer library by using a build framework. 
In this documentation we cover Maven and Gradle.

You can check the last version of the library in this [link](https://search.maven.org/search?q=a:dimmer-local)

Using Maven:
```xml
<dependency>
    <groupId>com.github.cloudyrock.dimmer</groupId>
    <artifactId>dimmer-local</artifactId>
    <version>LATEST_VERSION</version>
</dependency>
```

Using Gradle:
```groovy
compile group: 'com.github.cloudyrock.dimmer', name: 'dimmer-local', version: 'LATEST_VERSION'
```

In order to make aspects work, so Dimmer can do its job, you need to add some trivial configuration to your 
build script.

Using Maven:
```xml
<plugin>
    <groupId>org.codehaus.mojo</groupId>
    <artifactId>aspectj-maven-plugin</artifactId>
    <version>1.4</version>
    <dependencies>
        <dependency>
            <groupId>org.aspectj</groupId>
            <artifactId>aspectjrt</artifactId>
            <version>1.8.2</version>
        </dependency>
        <dependency>
            <groupId>org.aspectj</groupId>
            <artifactId>aspectjtools</artifactId>
            <version>1.8.2</version>
        </dependency>
    </dependencies>
    <executions>
        <execution>
            <phase>process-classes</phase>
            <goals>
                <goal>compile</goal>
            </goals>
        </execution>
    </executions>
    <configuration>
        <showWeaveInfo/>
        <forceAjcCompile>true</forceAjcCompile>
        <sources/>
        <weaveDirectories>
            <weaveDirectory>${project.build.directory}/classes</weaveDirectory>
        </weaveDirectories>
        <aspectLibraries>
            <aspectLibrary>
                <groupId>com.github.cloudyrock.dimmer</groupId>
                <artifactId>dimmer-local</artifactId>
			</aspectLibrary>
		</aspectLibraries>
		<source>1.8</source>
		<target>1.8</target>
	</configuration>
</plugin>
```



## Lets code see some code?
Once you have imported the required dependency and configure aspectj plugin, you are ready to code some code

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
As you can see in 'How does it work?', the example is using 'featureWithValue'. What this does
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
Sometimes throwing an exception or returning a fixed value is not flexible enough. You may need to return a dynamic value 
or, maybe, in some situations you want to return a value, while in others you want to throw an exception. It's fine, dimmer gives you 
all the flexibility you need with what we call 'behaviours'. 

Before providing a sample,lets clarify some concepts first.

- *FeatureInvocation:* It's an object which encapsulates the information regarding the invocation(signature, arguments, etc.). 
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

- *Behaviour:* As its name suggests, provides the capability to perform some dynamic actions for a given feature.
In Dimmer, a behaviour is Java 8 Function, which takes a FeatureInvocation as input parameter.
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
may prefer to throw your own exception. That's still possible with Dimmer, however it 
needs to be an unchecked exception(inherits from RuntimeException) and fulfill  at least one 
of the following requirements:

- Provides a constructor which takes a FeatureInvocation as parameter
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

### Environments 
Because any decent project has to deal with environment, Dimmer need to provide support to it.
The idea is that you configure all the possible environments you may have and then build with the current one.
```java
public class Main {

    private static final String FEATURE_NAME = "feature_name";

    private static final String ENV1 = "e1", ENV2 = "e2", ENV3 = "e3", ENV_NOT_CONFIGURED = "e_n_c";

    //args[0] provides the environment where the application is running
    public static void main(String... args) {
        DimmerBuilder
                .local()
                .environments(ENV1, ENV2)//ENV1 or ENV2
                .featureWithDefaultException(FEATURE_NAME)
                .environments(ENV3)//ENV3
                .featureWithValue(FEATURE_NAME, "value for environment ENV3")
                .build(args[0]);

        new Main().runFeaturedMethod();
    }

    @DimmerFeature(FEATURE_NAME)
    private String runFeaturedMethod() {
        return "real value";
    }
}
```
As you can see in the code above, we have configured the feature behaviour depending on the environment the application 
is running on(value of args[0]) to...
- throw a default exception when ENV1 or ENV2
- return 'value for environment ENV3' if ENV3
- and what happens if it's running on ENV_NOT_CONFIGURED?... It just calls the real method, so it will return 'real value'

You may be confused with the use of 'defaultEnvironment()' and 'buildWithDefaultEnvironment()'. This is in the cases you don't care
about environments or all your environments require the same configuration.


### Conditional toggling
We have seen that environments provide some flexible, but still easy, way to deal with environments.
However, sometimes this is not enough. Sometimes you want your feature depending on a dynamic property, instead of an static environment.
To solve this, in all the methods provided by the builder to configure features(featureWithValue, featureWithBehaviour, etc.) you can
add a boolean flag which indicate if you want to provide the given behaviour to the feature(flag is true) or you just want to ignore(flag is false)
```java
public void dimmerConfiguration(boolean toggledOff) {
    DimmerBuilder
        .local()
        .defaultEnvironment()
        .featureWithDefaultException(toggledOff, FEATURE_NAME)//Taken into account only if toggledOff is true
        .buildWithDefaultEnvironment();
}
```

# STILL PENDING IN THIS PULL REQUEST
USE CASES
KNOWN ISSUES -> lombok intellij
logging-> Just mention that uses slf4j and will print a warning if no binding implementation s provided