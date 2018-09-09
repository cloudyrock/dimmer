package com.github.cloudyrock.dimmer.samples.controller.model;

import org.springframework.boot.jackson.JsonComponent;

import java.io.Serializable;

@JsonComponent
public class UserApiRequest implements Serializable{
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
