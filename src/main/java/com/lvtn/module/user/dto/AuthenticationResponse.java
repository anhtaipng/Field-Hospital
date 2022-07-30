package com.lvtn.module.user.dto;

import lombok.Data;

@Data
public class AuthenticationResponse {
    private String accessToken;
    private String refreshToken;
    private UserDtoResponse userDtoResponse;
}
