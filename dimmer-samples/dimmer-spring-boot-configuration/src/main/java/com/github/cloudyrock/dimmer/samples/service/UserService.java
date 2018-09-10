package com.github.cloudyrock.dimmer.samples.service;

import com.github.cloudyrock.dimmer.samples.repository.User;

import java.util.List;

public interface UserService {


    List<User> getListOfUsers();

    User createUser(User user);

}
