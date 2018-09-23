package com.github.cloudyrock.dimmer.samples.service;

import com.github.cloudyrock.dimmer.samples.repository.User;
import com.github.cloudyrock.dimmer.samples.repository.UserRepository;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.BDDAssertions.then;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

@RunWith(MockitoJUnitRunner.class)
public class UserServiceUTest {

    public static final String TEST_USER = "TEST";

    @Mock
    private UserRepository mockUserRepository;
    @InjectMocks
    private UserServiceImpl userService;

    @Before
    public void setUp() {
        initMocks(this);
    }

    @After
    public void tearDown() {
        verifyNoMoreInteractions(mockUserRepository);
    }

    @Test
    public void shouldCreateUser() {
        when(mockUserRepository.saveAndFlush(any(User.class))).thenReturn(new User());
        final User user = userService.createUser(new User());
        then(user).isNotNull();
        verify(mockUserRepository, times(1)).saveAndFlush(any(User.class));
    }

    @Test
    public void shouldReturnListOfUsersWhenListNotEmpty() {
        when(mockUserRepository.findAll()).thenReturn(new ArrayList<>());
        final List user = userService.getListOfUsers();
        then(user).isNotNull();
        verify(mockUserRepository, times(1)).findAll();
    }

    @Test
    public void shouldSearchByUserName() {
        when(mockUserRepository.findByName(TEST_USER)).thenReturn(Optional.of(new User()));
        final Optional<User> user = userService.searchByUserName(TEST_USER);
        then(user).isNotNull();
        verify(mockUserRepository, times(1)).findByName(TEST_USER);
    }

}
