package com.github.cloudyrock.dimmer.spring;

import com.github.cloudyrock.dimmer.ConfigService;
import com.github.cloudyrock.dimmer.DimmerConfigResponse;
import com.github.cloudyrock.dimmer.DimmerConfigServiceSpring;
import com.google.common.base.Preconditions;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.Set;

@RestController
public class ConfigController implements DimmerConfigServiceSpring {

    private final ConfigService configService;

    ConfigController(ConfigService configService) {
        Preconditions.checkArgument(configService != null);
        this.configService = configService;
    }

    @Override
    public DimmerConfigResponse getConfigByEnvironment(@PathVariable("environment") String environment) {
        final Set<String> features = configService.getInterceptedFeaturesByEnvironment(environment);
        return new DimmerConfigResponse(environment, features);
    }

}