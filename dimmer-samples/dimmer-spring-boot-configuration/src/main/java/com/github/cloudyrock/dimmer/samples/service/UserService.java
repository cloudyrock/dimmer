package com.github.cloudyrock.dimmer.samples.service;

import com.github.cloudyrock.dimmer.samples.repository.User;

import java.util.List;
import java.util.Optional;

public interface UserService {

    List<User> getListOfUsers();

    User createUser(User user);

    Optional<User> searchByUserName(String user);

}
