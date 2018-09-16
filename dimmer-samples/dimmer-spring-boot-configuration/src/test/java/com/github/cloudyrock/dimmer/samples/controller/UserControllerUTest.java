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

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.anyList;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

@RunWith(MockitoJUnitRunner.class)
public class UserControllerUTest {

    public static final String USER1 = "DIMMER";

    //This line helps initialising a default mocked environment for Dimmer annotations.
    @Mock
    private FeatureExecutor featureExecutor = DimmerBuilder.local().defaultEnvironment().buildWithDefaultEnvironment();

    @Mock
    private UserService mockUserService;

    @Mock
    private UserControllerMapper mockUserControllerMapper;

    @InjectMocks
    private UserController userController;

    @Before
    public void setUp() {
        initMocks(this);
    }

    @After
    public void tearDown() {
        verifyNoMoreInteractions(mockUserService, mockUserControllerMapper);
    }

    @Test
    public void testGetsUsersWhenNoUsersExist() {
        when(mockUserService.getListOfUsers()).thenReturn(new ArrayList<>());
        when(mockUserControllerMapper.toResponse(anyList())).thenReturn(new ArrayList<>());
        final List<UserApiResponse> userApiResponseList = userController.getUsers();
        assertTrue(userApiResponseList.isEmpty());
        verify(mockUserService, times(1)).getListOfUsers();
        verify(mockUserControllerMapper, times(1)).toResponse(anyList());
    }

    @Test
    public void testGetsUsersWhenUsersExist() {
        when(mockUserService.getListOfUsers()).thenReturn(getUserListWithUser());

        final ArrayList<UserApiResponse> conversionResult = new ArrayList<>();
        conversionResult.add(new UserApiResponse(1L, USER1));
        when(mockUserControllerMapper.toResponse(anyList())).thenReturn(conversionResult);

        final List<UserApiResponse> userApiResponseList = userController.getUsers();

        assertThat(userApiResponseList.size(), is(1));
        assertThat(userApiResponseList.get(0).getName(), is(USER1));
        verify(mockUserService, times(1)).getListOfUsers();
        verify(mockUserControllerMapper, times(1)).toResponse(anyList());
    }

    @Test
    public void testAddUser() {
        final UserApiRequest userApiRequest = new UserApiRequest(USER1);

        final User user = createUser();
        when(mockUserService.createUser(any())).thenReturn(user);

        final UserApiResponse toResult = new UserApiResponse(1L, USER1);
        when(mockUserControllerMapper.toResponse(user)).thenReturn(toResult);

        final UserApiResponse userApiResponse = userController.addUser(userApiRequest);

        assertNotNull(userApiResponse);
        assertThat(userApiRequest.getName(), is(USER1));
        verify(mockUserService, times(1)).createUser(any());
        verify(mockUserControllerMapper, times(1)).fromRequest(any());
        verify(mockUserControllerMapper, times(1)).toResponse(user);
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