package com.github.cloudyrock.dimmer.test;

import com.github.cloudyrock.dimmer.DimmerFeatureService;
import com.github.cloudyrock.dimmer.DimmerFeatureServiceBuilder;
import com.github.cloudyrock.dimmer.FeatureInvocation;
import org.junit.Test;

import java.util.function.Function;

public class DimmerServerConfigurationTest {


    Function<FeatureInvocation,?> feature1 = featureInvocation -> 1;

    @Test
    public void setsConfigurationToServerBuilder() throws Throwable {

        DimmerFeatureService dimmerFeatureService = DimmerFeatureServiceBuilder.createInstance()
                .environments("env1")
                    .featureWithBehaviour("f1", feature1)
                    .featureWithBehaviour("f2", featureInvocation -> 2)
                    .featureWithBehaviour("f3", featureInvocation -> 3).build();

        //TODO: finish test
//        dimmerFeatureService.invokeFeatureForEnvironment("env1", "f1", FeatureInvocation);
    }
}
