package com.github.cloudyrock.dimmer.samples.controller.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public final class UserApiRequest {

    @JsonProperty("name")
    private String name;

    public UserApiRequest() {
    }

    public UserApiRequest(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
