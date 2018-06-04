# FEATURES
- Handle exceptions in Aspect when the returned object doesn't match the real method's signature
- Replace String with a more static way of passing feature to @FeatureCheck: enum(problem enums cannot be casted) or something
- Add returning type to FeatureInvocation
- @FeatureOffXXX precedes @FeatureCheck → Put in README and JavaDoc
- Make DimmerConfiguration non static???
- Generic support for environments
- Camel support...maybe just providing a Procesor
- Provide configuration for Spring as well as stand alone apps → this should be a separated library
    - Spring environment
- Be able to clean feature configuration. This will allow runtime configuration. But won’t survived though execution(no persisted)
- Provide enough information to the exception: class, method, parameters, name of feature, etc.

- Persist configuration? → Challenges
    - store/load functions
    - Different types of database
    - Different frameworks
    
- separate in different libraries:
    - dimmer-core: It would have the main classes and configuration to be used fot the other libraries.
    - dimmer-standalone: It would have configuration and classes to be used in a standalone application
    - dimmer-spring: Library for spring integration
    - dimmer-camel: Library for camel integration



# OPS, MANAGEMENT, ETC
- Configure GITHUB: PR requirements, etc.
- publish Maven repository
- Travis
- JavaDoc
- README
    - Description
    - Configuration
    - How to collaborate
    - Standards
        - Length size to 90. To split the screen in 2: test and class
        - Optionals for nulls
        - Only RuntimeExceptions
        - Test method names
        - Final modifier?

# QUESTIONS
- When not using Spring, pom has to be configured by adding aspectj-maven-plugin. Does this allow to add other aspect libraries?
- Problems with Lombock and other aspectj libraries