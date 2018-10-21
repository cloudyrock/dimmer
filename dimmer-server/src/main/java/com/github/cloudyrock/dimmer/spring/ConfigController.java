package com.github.cloudyrock.dimmer.spring;

import com.github.cloudyrock.dimmer.ConfigService;
import com.github.cloudyrock.dimmer.DimmerConfigResponse;
import com.github.cloudyrock.dimmer.DimmerConfigServiceSpring;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ConfigController implements DimmerConfigServiceSpring {

    private final ConfigService configService;

    ConfigController(ConfigService configService) {
        this.configService = configService;
    }

    @Override
    public DimmerConfigResponse getConfigByEnvironment(@PathVariable("environment") String environment) {
        return configService.getValue(environment);
    }

}
