package com.github.cloudyrock.dimmer.samples.configuration;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_IMPLEMENTED)
public class NotImplementedException extends RuntimeException {

    public static final String FEATURE_IS_WORK_IN_PROGRESS = "Feature is work in progress :)";

    public NotImplementedException() {
        super(FEATURE_IS_WORK_IN_PROGRESS);
    }
}
