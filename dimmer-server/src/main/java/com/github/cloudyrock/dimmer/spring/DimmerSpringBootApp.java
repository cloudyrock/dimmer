package com.github.cloudyrock.dimmer.spring;


import com.github.cloudyrock.dimmer.ConfigRepository;
import com.github.cloudyrock.dimmer.ConfigService;
import com.github.cloudyrock.dimmer.FileConfigRepositoryImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.PropertySource;
import org.springframework.web.filter.CommonsRequestLoggingFilter;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;


@Configuration
@EnableConfigurationProperties({FeatureConfig.class})
@EnableAutoConfiguration
public class DimmerSpringBootApp {

    //TODO: This should be removed without problem. Check
    @Autowired
    private FeatureConfig featureConfig;

    public static void run(String[] args) {
        SpringApplication.run(DimmerSpringBootApp.class, args);
    }

    @Bean
    ConfigController configController(ConfigService configService) {
        return new ConfigController(configService);
    }

    @Bean
    ConfigService configService(ConfigRepository configRepository) {
        return new ConfigService(configRepository);
    }

    @Bean
    ConfigRepository configRepository(FeatureConfig featureConfig) {
        return new FileConfigRepositoryImpl(featureConfig.getEnvironments());
    }
}
