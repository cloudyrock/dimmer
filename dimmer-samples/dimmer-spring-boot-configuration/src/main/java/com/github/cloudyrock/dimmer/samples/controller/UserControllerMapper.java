package com.github.cloudyrock.dimmer.samples.controller;

import com.github.cloudyrock.dimmer.samples.controller.model.UserApiRequest;
import com.github.cloudyrock.dimmer.samples.controller.model.UserApiResponse;
import com.github.cloudyrock.dimmer.samples.repository.User;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

public final class UserControllerMapper {

    final static Function<User, UserApiResponse> userUserApiResponseFunction =
            user -> createNewUserApiResponseFromUser(user);

    public static User convertUserApiRequestToUser(UserApiRequest userApiRequest) {
        final User user = new User();
        user.setName(userApiRequest.getName());
        return user;
    }

    public static UserApiResponse convertToUserResponse(User user) {
        return createNewUserApiResponseFromUser(user);
    }

    public static List<UserApiResponse> convertToListUserApiResponse(List<User> listOfUsers) {

        return listOfUsers.stream().
                map(userUserApiResponseFunction)
                .collect(Collectors.toList());
    }

    private static UserApiResponse createNewUserApiResponseFromUser(User user){
        final UserApiResponse userApiResponse = new UserApiResponse();
        userApiResponse.setName(user.getName());
        userApiResponse.setId(user.getId());
        return userApiResponse;
    }
}
