package com.github.cloudyrock.dimmer.spring;


import com.github.cloudyrock.dimmer.ConfigRepository;
import com.github.cloudyrock.dimmer.ConfigService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.filter.CommonsRequestLoggingFilter;

@Configuration
@SpringBootApplication
public class DimmerSpringBootApp {

    public static void run(String[] args) {
        SpringApplication.run(DimmerSpringBootApp.class, args);
    }

    @Bean
    ConfigService configService(ConfigRepository configRepository) {
        return new ConfigService(configRepository);
    }

    @Bean
    ConfigRepository configRepository() {
        //todo create mapper instance and get propertyFile
        return null;
    }



    //TODO remove this
    @Bean
    public CommonsRequestLoggingFilter logFilter() {
        CommonsRequestLoggingFilter filter = new CommonsRequestLoggingFilter();
        filter.setIncludeClientInfo(true);
        filter.setIncludeQueryString(true);
        filter.setIncludePayload(true);
        filter.setMaxPayloadLength(10000);
        filter.setIncludeHeaders(true);
        filter.setAfterMessagePrefix("REQUEST DATA : ");
        return filter;
    }
}
