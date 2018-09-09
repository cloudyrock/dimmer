package com.github.cloudyrock.dimmer.samples.controller.model;

import org.springframework.boot.jackson.JsonComponent;

import java.io.Serializable;

@JsonComponent
public class UserApiResponse implements Serializable {

    private Long id;
    private String name;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
