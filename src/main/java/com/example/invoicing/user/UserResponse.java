package com.example.invoicing.user;

import java.time.LocalDateTime;
import java.util.UUID;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserResponse {
    private UUID id;
    private LocalDateTime createdAt;
    private LocalDateTime lastLoginAt;
    private String email;
    private String firstName;
    private String lastName;
    private Integer failedLoginAttempts;
    private LocalDateTime lockedUntil;

    public static UserResponse from(UserEntity userEntity) {
        UserResponse userResponse = new UserResponse();

        userResponse.setId(userEntity.getId());
        userResponse.setCreatedAt(userEntity.getCreatedAt());
        userResponse.setLastLoginAt(userEntity.getLastLoginAt());
        userResponse.setEmail(userEntity.getEmail());
        userResponse.setFirstName(userEntity.getFirstName());
        userResponse.setLastName(userEntity.getLastName());
        userResponse.setFailedLoginAttempts(userEntity.getFailedLoginAttempts());
        userResponse.setLockedUntil(userEntity.getLockedUntil());

        return userResponse;
    }
}
