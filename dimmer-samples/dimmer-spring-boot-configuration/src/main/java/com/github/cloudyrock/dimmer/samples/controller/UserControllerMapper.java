package com.github.cloudyrock.dimmer.samples.controller;

import com.github.cloudyrock.dimmer.samples.controller.model.UserApiRequest;
import com.github.cloudyrock.dimmer.samples.controller.model.UserApiResponse;
import com.github.cloudyrock.dimmer.samples.repository.User;

import java.util.List;
import java.util.stream.Collectors;

public class UserControllerMapper {

    public User fromRequest(UserApiRequest userApiRequest) {
        final User user = new User();
        user.setName(userApiRequest.getName());
        return user;
    }

    public UserApiResponse toResponse(User user) {
        return createNewUserApiResponseFromUser(user);
    }

    public List<UserApiResponse> toResponse(List<User> listOfUsers) {

        return listOfUsers.stream().
                map(user -> createNewUserApiResponseFromUser(user))
                .collect(Collectors.toList());
    }

    private static UserApiResponse createNewUserApiResponseFromUser(User user){
        return new UserApiResponse(user.getId(),user.getName());
    }
}
