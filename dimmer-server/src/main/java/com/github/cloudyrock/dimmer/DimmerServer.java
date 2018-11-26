package com.github.cloudyrock.dimmer;

import com.github.cloudyrock.dimmer.spring.DimmerSpringBootApp;

public final class DimmerServer {

    private DimmerServer() {
    }

    public static void main(String[] args) {
        DimmerServer.run(DimmerServerType.SPRING, args);
    }

    private static void  run(DimmerServerType implementation, String[] args) {
        switch (implementation) {
            case SPRING:
                DimmerSpringBootApp.run(args);
                break;
            default:
                throw new IllegalArgumentException(String.format("Implementation %s not recognised", implementation));
        }
    }

}

//TODO: add tests: unit and integrations tests
//TODO: manage exceptions-> properties not passed, wrong configuration, environment not found, etc.
//TODO: add jacoco
//TODO: README hot to config
//TODO: docker
