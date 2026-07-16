package com.prince.flexisaf.controller;

import com.prince.flexisaf.dto.UserRequest;
import com.prince.flexisaf.dto.UserResponse;
import com.prince.flexisaf.service.UserService;
import com.prince.flexisaf.util.TestDataFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserControllerTest {

    @Mock
    private UserService userService;

    @InjectMocks
    private UserController userController;

    private UserRequest userRequest;
    private UserResponse userResponse;

    @BeforeEach
    void setUp() {
        userRequest = TestDataFactory.createValidUserRequest();
        userResponse = UserResponse.builder()
                .userId(1L)
                .email(userRequest.getEmail())
                .firstName(userRequest.getFirstName())
                .lastName(userRequest.getLastName())
                .role(userRequest.getRole())
                .enabled(true)
                .build();
    }

    @Test
    void createUser_ShouldReturnUserResponse_WhenValidRequest() {
        when(userService.createUser(any(UserRequest.class))).thenReturn(userResponse);

        var response = userController.createUser(userRequest);

        assertThat(response).isNotNull();
        assertThat(response.getBody().getEmail()).isEqualTo(userResponse.getEmail());
    }

    @Test
    void getUserById_ShouldReturnUserResponse_WhenUserExists() {
        when(userService.getUserById(1L)).thenReturn(userResponse);

        var response = userController.getUser(1L);

        assertThat(response).isNotNull();
        assertThat(response.getBody().getUserId()).isEqualTo(userResponse.getUserId());
    }

    @Test
    void getUserByEmail_ShouldReturnUserResponse_WhenUserExists() {
        when(userService.getUserByEmail(userResponse.getEmail())).thenReturn(userResponse);

        var response = userController.getUserByEmail(userResponse.getEmail());

        assertThat(response).isNotNull();
        assertThat(response.getBody().getEmail()).isEqualTo(userResponse.getEmail());
    }

    @Test
    void updateUser_ShouldReturnUpdatedUserResponse_WhenValidRequest() {
        when(userService.updateUser(any(Long.class), any(UserRequest.class))).thenReturn(userResponse);

        var response = userController.updateUser(1L, userRequest);

        assertThat(response).isNotNull();
        assertThat(response.getBody().getUserId()).isEqualTo(userResponse.getUserId());
    }
}
