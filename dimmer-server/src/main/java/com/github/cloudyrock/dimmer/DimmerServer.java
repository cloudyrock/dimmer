package com.github.cloudyrock.dimmer;

import com.github.cloudyrock.dimmer.spring.DimmerSpringBootApp;

public final class DimmerServer {

    public static void  run(DimmerServerType implementation, String[] args) {
        switch (implementation) {
            case SPRING:
                DimmerSpringBootApp.run(args);
                break;
            default:
                throw new IllegalArgumentException(String.format("Implementation %s not recognised", implementation));
        }
    }

}
