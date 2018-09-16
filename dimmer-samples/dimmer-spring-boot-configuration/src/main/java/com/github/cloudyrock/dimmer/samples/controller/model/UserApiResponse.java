package com.github.cloudyrock.dimmer.samples.controller.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public final class UserApiResponse {

    @JsonProperty("id")
    private final Long id;

    @JsonProperty("name")
    private final String name;

    public UserApiResponse(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

}
