package com.github.cloudyrock.dimmer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.reactive.config.EnableWebFlux;


@SpringBootApplication
@EnableWebFlux
public class DimmerServerApplication {

    public static void main(String[] args) throws Exception {
        SpringApplication.run(DimmerServerApplication.class);
    }
}
