package com.github.cloudyrock.dimmer.samples;

import com.github.cloudyrock.dimmer.DimmerBuilder;
import com.github.cloudyrock.dimmer.FeatureExecutor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

@Configuration
public class DimmerConfiguration {

    @Autowired
    private Environment environment;

    @Bean
    public FeatureExecutor dimmerBuilder() {
        System.out.println("Starting Dimmer Builder on environment " + environment.getActiveProfiles()[0]);
        return DimmerBuilder.local()
                .environments("default")
                .featureWithBehaviour("GET_USERS", featureInvocation -> "NOT IMPLEMENTED")
                .environments("dev")
                .featureWithBehaviour("GET_USERS", featureInvocation -> "NOT IMPLEMENTED")
                .environments("prod")
                .featureWithBehaviour("ADD_USER", featureInvocation -> "NOT IMPLEMENTED")
                //TODO: Careful here - spring allows to set multiple profiles whilst Dimmer only builds one environment
                .build(environment.getActiveProfiles()[0]);
    }

}
