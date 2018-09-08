package com.github.cloudyrock.dimmer.samples.controller;

import com.github.cloudyrock.dimmer.DimmerFeature;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class UserController {

    private static final String ADD_USER = "ADD_USER";
    private static final String GET_USERS = "GET_USERS";

    @DimmerFeature(GET_USERS)
    @RequestMapping
    public List<UserReponse> getUsers(){
        return null;
    }

    @DimmerFeature(ADD_USER)
    @RequestMapping
    public List<UserReponse> addUser(UserRequest userRequest){
        return null;
    }
}
