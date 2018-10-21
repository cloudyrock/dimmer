package com.github.cloudyrock.dimmer;

import java.time.LocalDateTime;

public class ConfigService {


    public DimmerConfigResponse getValue(String environment) {
        final String value = "OK -  " + environment + LocalDateTime.now().toString();
        return new DimmerConfigResponse(value);
    }
}
