package com.prince.flexisaf.service;

import com.prince.flexisaf.dto.UserRequest;
import com.prince.flexisaf.dto.UserResponse;
import com.prince.flexisaf.entity.User;
import com.prince.flexisaf.exception.ResourceNotFoundException;
import com.prince.flexisaf.repository.UserRepository;
import com.prince.flexisaf.util.TestDataFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    private User testUser;
    private UserRequest userRequest;

    @BeforeEach
    void setUp() {
        testUser = TestDataFactory.createTestUser();
        userRequest = TestDataFactory.createValidUserRequest();
    }

    @Test
    void createUser_ShouldReturnUserResponse_WhenValidRequest() {
        when(userRepository.existsByEmail(userRequest.getEmail())).thenReturn(false);
        when(passwordEncoder.encode(anyString())).thenReturn("$2a$10$encodedPassword");
        when(userRepository.save(any(User.class))).thenReturn(testUser);

        UserResponse response = userService.createUser(userRequest);

        assertThat(response).isNotNull();
        assertThat(response.getEmail()).isEqualTo(userRequest.getEmail());
        assertThat(response.getFirstName()).isEqualTo(userRequest.getFirstName());
        assertThat(response.getLastName()).isEqualTo(userRequest.getLastName());
        assertThat(response.getRole()).isEqualTo(userRequest.getRole());

        verify(userRepository).existsByEmail(userRequest.getEmail());
        verify(passwordEncoder).encode(userRequest.getPassword());
        verify(userRepository).save(any(User.class));
    }

    @Test
    void createUser_ShouldThrowException_WhenEmailExists() {
        when(userRepository.existsByEmail(userRequest.getEmail())).thenReturn(true);

        assertThatThrownBy(() -> userService.createUser(userRequest))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Email already exists");

        verify(userRepository).existsByEmail(userRequest.getEmail());
        verify(passwordEncoder, never()).encode(anyString());
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void getUserById_ShouldReturnUserResponse_WhenUserExists() {
        when(userRepository.findById(testUser.getUserId())).thenReturn(Optional.of(testUser));

        UserResponse response = userService.getUserById(testUser.getUserId());

        assertThat(response).isNotNull();
        assertThat(response.getUserId()).isEqualTo(testUser.getUserId());
        assertThat(response.getEmail()).isEqualTo(testUser.getEmail());

        verify(userRepository).findById(testUser.getUserId());
    }

    @Test
    void getUserById_ShouldThrowException_WhenUserNotFound() {
        when(userRepository.findById(999L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> userService.getUserById(999L))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("User not found with id: 999");

        verify(userRepository).findById(999L);
    }

    @Test
    void getUserByEmail_ShouldReturnUserResponse_WhenUserExists() {
        when(userRepository.findByEmail(testUser.getEmail())).thenReturn(Optional.of(testUser));

        UserResponse response = userService.getUserByEmail(testUser.getEmail());

        assertThat(response).isNotNull();
        assertThat(response.getEmail()).isEqualTo(testUser.getEmail());

        verify(userRepository).findByEmail(testUser.getEmail());
    }

    @Test
    void getUserByEmail_ShouldThrowException_WhenUserNotFound() {
        when(userRepository.findByEmail("nonexistent@example.com")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> userService.getUserByEmail("nonexistent@example.com"))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("User not found with email: nonexistent@example.com");

        verify(userRepository).findByEmail("nonexistent@example.com");
    }

    @Test
    void getAllUsers_ShouldReturnListOfUserResponses() {
        when(userRepository.findAll()).thenReturn(java.util.List.of(testUser));

        var responses = userService.getAllUsers();

        assertThat(responses).isNotNull();
        assertThat(responses).hasSize(1);
        assertThat(responses.get(0).getEmail()).isEqualTo(testUser.getEmail());

        verify(userRepository).findAll();
    }

    @Test
    void updateUser_ShouldReturnUpdatedUserResponse_WhenValidRequest() {
        UserRequest updateRequest = TestDataFactory.createValidUserRequest();
        updateRequest.setEmail("updated@example.com");
        updateRequest.setFirstName("Updated");
        updateRequest.setPassword("NewPassword123");

        when(userRepository.findById(testUser.getUserId())).thenReturn(Optional.of(testUser));
        when(userRepository.existsByEmail("updated@example.com")).thenReturn(false);
        when(userRepository.save(any(User.class))).thenReturn(testUser);

        UserResponse response = userService.updateUser(testUser.getUserId(), updateRequest);

        assertThat(response).isNotNull();
        verify(userRepository).findById(testUser.getUserId());
        verify(userRepository).save(any(User.class));
    }

    @Test
    void updateUser_ShouldThrowException_WhenUserNotFound() {
        when(userRepository.findById(999L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> userService.updateUser(999L, userRequest))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("User not found with id: 999");

        verify(userRepository).findById(999L);
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void updateUser_ShouldThrowException_WhenEmailAlreadyExists() {
        UserRequest updateRequest = TestDataFactory.createValidUserRequest();
        updateRequest.setEmail("different@example.com");

        when(userRepository.findById(testUser.getUserId())).thenReturn(Optional.of(testUser));
        when(userRepository.existsByEmail("different@example.com")).thenReturn(true);

        assertThatThrownBy(() -> userService.updateUser(testUser.getUserId(), updateRequest))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Email already exists");

        verify(userRepository).findById(testUser.getUserId());
        verify(userRepository).existsByEmail("different@example.com");
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void deleteUser_ShouldDeleteUser_WhenUserExists() {
        when(userRepository.findById(testUser.getUserId())).thenReturn(Optional.of(testUser));
        doNothing().when(userRepository).delete(any(User.class));

        userService.deleteUser(testUser.getUserId());

        verify(userRepository).findById(testUser.getUserId());
        verify(userRepository).delete(testUser);
    }

    @Test
    void deleteUser_ShouldThrowException_WhenUserNotFound() {
        when(userRepository.findById(999L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> userService.deleteUser(999L))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("User not found with id: 999");

        verify(userRepository).findById(999L);
        verify(userRepository, never()).delete(any(User.class));
    }
}
