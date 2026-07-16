package com.prince.flexisaf.integration;

import com.prince.flexisaf.dto.UserRequest;
import com.prince.flexisaf.entity.User;
import com.prince.flexisaf.enums.Role;
import com.prince.flexisaf.repository.UserRepository;
import com.prince.flexisaf.service.UserService;
import com.prince.flexisaf.util.TestDataFactory;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("test")
class UserIntegrationTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private User testUser;

    @BeforeEach
    void setUp() {
        testUser = User.builder()
                .email("integration@example.com")
                .password(passwordEncoder.encode("Password123"))
                .firstName("Integration")
                .lastName("Test")
                .role(Role.USER)
                .enabled(true)
                .accountNonExpired(true)
                .accountNonLocked(true)
                .credentialsNonExpired(true)
                .build();
        userRepository.save(testUser);
    }

    @AfterEach
    void tearDown() {
        userRepository.deleteAll();
    }

    @Test
    void createUserIntegration_ShouldCreateAndRetrieveUser() {
        UserRequest request = TestDataFactory.createValidUserRequest();
        request.setEmail("newuser@example.com");

        var response = userService.createUser(request);

        assertThat(response).isNotNull();
        assertThat(response.getEmail()).isEqualTo("newuser@example.com");

        // Verify user was saved to database
        User savedUser = userRepository.findByEmail("newuser@example.com").orElse(null);
        assertThat(savedUser).isNotNull();
        assertThat(savedUser.getEmail()).isEqualTo("newuser@example.com");
    }

    @Test
    void getUserIntegration_ShouldRetrieveExistingUser() {
        var response = userService.getUserById(testUser.getUserId());

        assertThat(response).isNotNull();
        assertThat(response.getUserId()).isEqualTo(testUser.getUserId());
        assertThat(response.getEmail()).isEqualTo(testUser.getEmail());
    }

    @Test
    void getUserByEmailIntegration_ShouldRetrieveExistingUser() {
        var response = userService.getUserByEmail(testUser.getEmail());

        assertThat(response).isNotNull();
        assertThat(response.getEmail()).isEqualTo(testUser.getEmail());
        assertThat(response.getFirstName()).isEqualTo(testUser.getFirstName());
    }

    @Test
    void getAllUsersIntegration_ShouldReturnAllUsers() {
        var responses = userService.getAllUsers();

        assertThat(responses).isNotNull();
        assertThat(responses).hasSize(1);
        assertThat(responses.get(0).getEmail()).isEqualTo(testUser.getEmail());
    }

    @Test
    void updateUserIntegration_ShouldUpdateExistingUser() {
        UserRequest updateRequest = new UserRequest();
        updateRequest.setEmail(testUser.getEmail());
        updateRequest.setPassword("NewPassword123");
        updateRequest.setFirstName("Updated");
        updateRequest.setLastName("Name");
        updateRequest.setRole(Role.USER);

        var response = userService.updateUser(testUser.getUserId(), updateRequest);

        assertThat(response).isNotNull();
        assertThat(response.getFirstName()).isEqualTo("Updated");

        // Verify update in database
        User updatedUser = userRepository.findById(testUser.getUserId()).orElse(null);
        assertThat(updatedUser).isNotNull();
        assertThat(updatedUser.getFirstName()).isEqualTo("Updated");
    }

    @Test
    void deleteUserIntegration_ShouldDeleteUser() {
        userService.deleteUser(testUser.getUserId());

        // Verify deletion in database
        User deletedUser = userRepository.findById(testUser.getUserId()).orElse(null);
        assertThat(deletedUser).isNull();
    }
}
