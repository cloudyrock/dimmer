package com.github.cloudyrock.dimmer.samples.service;

import com.github.cloudyrock.dimmer.samples.repository.User;
import com.github.cloudyrock.dimmer.samples.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Optional;

public class UserServiceImpl implements UserService {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserServiceImpl.class);

    @Autowired
    private UserRepository userRepository;

    @Override
    public List<User> getListOfUsers() {
        LOGGER.info("Return list of users from repository");
        return userRepository.findAll();
    }

    @Override
    public User createUser(User user) {
        LOGGER.info("Create new user in repository");
        return userRepository.saveAndFlush(user);
    }

    @Override
    public Optional<User> searchByUserName(String user) {
        LOGGER.info("Create new user in repository");
        return userRepository.findByName(user);
    }
}