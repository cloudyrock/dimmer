package com.github.cloudyrock.dimmer.samples;

import com.github.cloudyrock.dimmer.samples.service.UserService;
import com.github.cloudyrock.dimmer.samples.service.UserServiceImpl;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class DimmerSpringBootConfigurationApplication {

    public static void main(String[] args) {
        SpringApplication.run(DimmerSpringBootConfigurationApplication.class, args);
    }

    @Bean
    public UserService userServiceBean(){
        return new UserServiceImpl();
    }
}
