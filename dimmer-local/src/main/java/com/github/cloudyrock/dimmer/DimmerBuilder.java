package com.github.cloudyrock.dimmer;

import java.util.Arrays;
import java.util.HashMap;

public class DimmerBuilder {

    public static DimmerEnvironmentConfigurable<DimmerLocalRunner> local() {
        return envs -> new DimmerLocalRunner(Arrays.asList(envs), new HashMap<>());
    }

    public static DimmerRemoteRunner remote(String url) {
        return new DimmerRemoteRunner(url);
    }
}
