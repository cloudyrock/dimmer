package com.github.cloudyrock.dimmer.samples.configuration;

import com.github.cloudyrock.dimmer.DimmerBuilder;
import com.github.cloudyrock.dimmer.FeatureExecutor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.env.Environment;


@Configuration
public class DimmerConfiguration {

    private static final Logger LOGGER = LoggerFactory.getLogger(DimmerConfiguration.class);

    public static final String ADD_USER = "ADD_USER";
    public static final String GET_USERS = "GET_USERS";

    public static final String DEV = "dev";
    public static final String DEFAULT = "default";
    public static final String PROD = "prod";

    //
//    @JsonComponent
//    class UserApiDimmerResponse extends UserApiResponse {
//        HttpStatus status = HttpStatus.NOT_IMPLEMENTED;
//
//        public HttpStatus getStatus() {
//            return status;
//        }
//    }

    @Bean
    @Profile(DEFAULT)
    public FeatureExecutor dimmerBuilderDev(Environment environment) {
        LOGGER.debug("Starting Dimmer Builder on environment {}", environment.getActiveProfiles()[0]);
        return DimmerBuilder.local()
                .environments(DEFAULT)
                .featureWithBehaviour(GET_USERS, featureInvocation -> "")
                .build(DEFAULT);
    }

    @Bean
    @Profile(DEV)
    public FeatureExecutor dimmerBuilderDefault(Environment environment) {
        LOGGER.debug("Starting Dimmer Builder on environment {}", environment.getActiveProfiles()[0]);
        return DimmerBuilder.local()
                .environments(DEV)
                .featureWithException(GET_USERS, NotImplementedException.class)
                .build(DEV);
    }

    @Bean
    @Profile(PROD)
    public FeatureExecutor dimmerBuilderProd(Environment environment) {
        LOGGER.debug("Starting Dimmer Builder on environment {}", environment.getActiveProfiles()[0]);
        return DimmerBuilder.local()
                .environments(PROD)
                .featureWithBehaviour(ADD_USER, featureInvocation -> "NOT IMPLEMENTED")
                .build(PROD);
    }

}
