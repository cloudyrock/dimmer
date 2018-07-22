package com.github.cloudyrock.dimmer;

import java.util.HashMap;
import java.util.HashSet;

public abstract class DimmerBuilder {


    public static DimmerRemoteBuilder remote() {
        return new DimmerRemoteBuilder();
    }


    public static DimmerLocalBuilder local() {
        return new DimmerLocalBuilder(new HashSet<>(), new HashMap<>());
    }


    public static DimmerServerBuilder server() {
        return new DimmerServerBuilder(new HashSet<>(), new HashMap<>());
    }


    /**
     * TODO change doc
     * If not initialised yet, it builds a the singleton DimmerProcessor instance
     * with the configured parameters.
     *
     * @return the DimmerProcessor instance.
     */
    public abstract  DimmerProcessor build();
}
