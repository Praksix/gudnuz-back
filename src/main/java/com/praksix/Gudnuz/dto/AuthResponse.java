package com.praksix.Gudnuz.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuthResponse {
    private String token;
    private String refreshToken;
    private String type = "Bearer";
    private String userId;
    private String username;
    private String email;
    private long expiresIn;
} 