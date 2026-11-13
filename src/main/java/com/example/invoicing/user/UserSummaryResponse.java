package com.example.invoicing.user;

import java.util.UUID;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserSummaryResponse {
    private UUID id;
    private String fullName;

    public static UserSummaryResponse from(UserEntity userEntity) {
        UserSummaryResponse userSummaryResponse = new UserSummaryResponse();

        userSummaryResponse.setId(userEntity.getId());
        userSummaryResponse.setFullName(userEntity.getFirstName() + " " + userEntity.getLastName());

        return userSummaryResponse;
    }
}
