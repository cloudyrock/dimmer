package com.github.cloudyrock.dimmer;

import java.util.Arrays;
import java.util.HashMap;

public final class DimmerBuilder {

    public static DimmerEnvironmentConfigurable configure() {
        return envList ->
                new DimmerConfigurableRunner(Arrays.asList(envList), new HashMap<>());
    }

    public static DimmerRemoteRunner runAsRemote(String uri) {
        return new DimmerRemoteRunner(uri);
    }

}
