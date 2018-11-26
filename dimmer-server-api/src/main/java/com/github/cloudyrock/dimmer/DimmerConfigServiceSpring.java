package com.github.cloudyrock.dimmer;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;

import static com.github.cloudyrock.dimmer.ApiPaths.ROOT_PATH;
import static com.github.cloudyrock.dimmer.ApiPaths.ENV_PATH;

@RequestMapping(value = ROOT_PATH
//        , produces = "application/json", consumes = "application/json"
)
public interface DimmerConfigServiceSpring {//extends DimmerConfigService {

//    @Override
    @GetMapping(ENV_PATH)
    @ResponseStatus(HttpStatus.OK)
    DimmerConfigResponse getConfigByEnvironment(String environment);

}
