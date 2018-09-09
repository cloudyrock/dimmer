package com.github.cloudyrock.dimmer.samples.controller;

import com.github.cloudyrock.dimmer.DimmerBuilder;
import com.github.cloudyrock.dimmer.FeatureExecutor;
import com.github.cloudyrock.dimmer.samples.controller.model.UserApiRequest;
import com.github.cloudyrock.dimmer.samples.controller.model.UserApiResponse;
import com.github.cloudyrock.dimmer.samples.repository.User;
import com.github.cloudyrock.dimmer.samples.service.UserService;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.context.annotation.Bean;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.initMocks;

@RunWith(MockitoJUnitRunner.class)
public class UserControllerTest {

    public static final String USER1 = "DIMMER";

    //This line helps initialising a default mocked environment for Dimmer annotations.
    @Mock
    private FeatureExecutor featureExecutor = DimmerBuilder.local().defaultEnvironment().buildWithDefaultEnvironment();

    @Mock
    private UserService mockUserService;

    @InjectMocks
    private UserController userController;

    @Before
    public void setUp() {
        initMocks(this);
    }

    @After
    public void tearDown() {
        verifyNoMoreInteractions(mockUserService);
    }

    @Test
    public void testGetsUsersWhenNoUsersExist() {
        when(mockUserService.getListOfUsers()).thenReturn(new ArrayList<>());
        final List<UserApiResponse> userApiResponseList = userController.getUsers();
        assertTrue(userApiResponseList.isEmpty());
        verify(mockUserService, times(1)).getListOfUsers();
    }

    @Test
    public void testGetsUsersWhenUsersExist() {
        when(mockUserService.getListOfUsers()).thenReturn(getUserListWithUser());
        final List<UserApiResponse> userApiResponseList = userController.getUsers();
        assertThat(userApiResponseList.size(), is(1));
        assertThat(userApiResponseList.get(0).getName(), is(USER1));
        verify(mockUserService, times(1)).getListOfUsers();
    }

    @Test
    public void testAddUser() {
        final UserApiRequest userApiRequest = new UserApiRequest();
        userApiRequest.setName(USER1);
        when(mockUserService.createUser(any())).thenReturn(createUser());

        final UserApiResponse userApiResponse = userController.addUser(userApiRequest);

        assertNotNull(userApiResponse);
        assertThat(userApiRequest.getName(), is(USER1));
        verify(mockUserService, times(1)).createUser(any());
    }

    private List getUserListWithUser() {
        final List userList = new ArrayList();
        userList.add(createUser());
        return userList;
    }

    private User createUser() {
        final User user = new User();
        user.setId(1L);
        user.setName(USER1);
        return user;
    }
}