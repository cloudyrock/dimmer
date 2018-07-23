package com.github.cloudyrock.dimmer;

import java.util.Arrays;
import java.util.HashMap;

public class DimmerLocalBuilder {

    public static DimmerEnvironmentConfigurable<DimmerLocalRunner> local() {
        return envs -> new DimmerLocalRunner(Arrays.asList(envs), new HashMap<>());
    }
}
