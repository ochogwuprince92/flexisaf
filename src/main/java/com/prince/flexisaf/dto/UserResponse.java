package com.prince.flexisaf.dto;

import com.prince.flexisaf.enums.Role;
import lombok.Builder;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Builder
public class UserResponse {
    private Long userId;
    private String email;
    private String firstName;
    private String lastName;
    private Role role;
    private Boolean enabled;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
