package com.praksix.Gudnuz.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuthDataDto {
    private UserDto user;
    private String token;
    private String refreshToken;
    private String type = "Bearer";
    private long expiresIn;
} 