package com.praksix.Gnudnuz.controller;

import com.praksix.Gnudnuz.dto.*;
import com.praksix.Gnudnuz.service.AuthenticationService;
import com.praksix.Gnudnuz.service.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationService service;
    private final JwtService jwtService;

    @PostMapping("/register")
    public ResponseEntity<?> register(
            @RequestBody RegisterRequest request
    ) {
        try {
            AuthResponse authResponse = service.register(request);
            
            // Créer le UserDto
            UserDto userDto = UserDto.builder()
                    .id(authResponse.getUserId())
                    .username(authResponse.getUsername())
                    .email(authResponse.getEmail())
                    .role("USER")
                    .status("ACTIVE")
                    .build();
            
            // Créer l'AuthDataDto
            AuthDataDto authData = AuthDataDto.builder()
                    .user(userDto)
                    .token(authResponse.getToken())
                    .refreshToken(authResponse.getRefreshToken())
                    .type(authResponse.getType())
                    .expiresIn(authResponse.getExpiresIn())
                    .build();
            
            // Créer la réponse API avec la structure attendue
            ApiResponseDto<AuthDataDto> response = ApiResponseDto.<AuthDataDto>builder()
                    .data(authData)
                    .success(true)
                    .message("Inscription réussie")
                    .build();
            
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            ErrorResponse error = ErrorResponse.builder()
                    .message(e.getMessage())
                    .error("Registration failed")
                    .status(HttpStatus.BAD_REQUEST.value())
                    .timestamp(LocalDateTime.now())
                    .path("/api/auth/register")
                    .build();
            return ResponseEntity.badRequest().body(error);
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> authenticate(
            @RequestBody LoginRequest request
    ) {
        try {
            AuthResponse authResponse = service.authenticate(request);
            
            // Créer le UserDto
            UserDto userDto = UserDto.builder()
                    .id(authResponse.getUserId())
                    .username(authResponse.getUsername())
                    .email(authResponse.getEmail())
                    .role("USER")
                    .status("ACTIVE")
                    .build();
            
            // Créer l'AuthDataDto
            AuthDataDto authData = AuthDataDto.builder()
                    .user(userDto)
                    .token(authResponse.getToken())
                    .refreshToken(authResponse.getRefreshToken())
                    .type(authResponse.getType())
                    .expiresIn(authResponse.getExpiresIn())
                    .build();
            
            // Créer la réponse API avec la structure attendue
            ApiResponseDto<AuthDataDto> response = ApiResponseDto.<AuthDataDto>builder()
                    .data(authData)
                    .success(true)
                    .message("Connexion réussie")
                    .build();
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            ErrorResponse error = ErrorResponse.builder()
                    .message("Email ou mot de passe incorrect")
                    .error("Authentication failed")
                    .status(HttpStatus.UNAUTHORIZED.value())
                    .timestamp(LocalDateTime.now())
                    .path("/api/auth/login")
                    .build();
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(error);
        }
    }

    @PostMapping("/refresh")
    public ResponseEntity<?> refreshToken(
            @RequestBody RefreshTokenRequest request
    ) {
        try {
            AuthResponse authResponse = service.refreshToken(request);
            
            // Créer le UserDto
            UserDto userDto = UserDto.builder()
                    .id(authResponse.getUserId())
                    .username(authResponse.getUsername())
                    .email(authResponse.getEmail())
                    .role("USER")
                    .status("ACTIVE")
                    .build();
            
            // Créer l'AuthDataDto
            AuthDataDto authData = AuthDataDto.builder()
                    .user(userDto)
                    .token(authResponse.getToken())
                    .refreshToken(authResponse.getRefreshToken())
                    .type(authResponse.getType())
                    .expiresIn(authResponse.getExpiresIn())
                    .build();
            
            // Créer la réponse API avec la structure attendue
            ApiResponseDto<AuthDataDto> response = ApiResponseDto.<AuthDataDto>builder()
                    .data(authData)
                    .success(true)
                    .message("Token rafraîchi avec succès")
                    .build();
            
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            ErrorResponse error = ErrorResponse.builder()
                    .message("Token de rafraîchissement invalide")
                    .error("Refresh token failed")
                    .status(HttpStatus.BAD_REQUEST.value())
                    .timestamp(LocalDateTime.now())
                    .path("/api/auth/refresh")
                    .build();
            return ResponseEntity.badRequest().body(error);
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(@RequestParam String userId) {
        try {
            service.logout(userId);
            return ResponseEntity.ok().build();
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/test")
    public ResponseEntity<String> test() {
        return ResponseEntity.ok("Auth endpoint accessible");
    }

    @GetMapping("/me")
    public ResponseEntity<?> getCurrentUser(
            @RequestHeader("Authorization") String authHeader
    ) {
        try {
            // Extraire le token du header Authorization
            String token = authHeader.substring(7); // Enlever "Bearer "
            String userEmail = service.getUserEmailFromToken(token);
            
            // Récupérer les informations utilisateur
            var user = service.getUserByEmail(userEmail);
            
            // Créer le UserDto
            UserDto userDto = UserDto.builder()
                    .id(user.getId())
                    .username(user.getUsername())
                    .email(user.getEmail())
                    .role(user.getRole())
                    .status(user.getStatus())
                    .build();
            
            // Créer la réponse API avec la structure attendue
            ApiResponseDto<UserDto> response = ApiResponseDto.<UserDto>builder()
                    .data(userDto)
                    .success(true)
                    .message("Informations utilisateur récupérées")
                    .build();
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            ErrorResponse error = ErrorResponse.builder()
                    .message("Token invalide ou expiré")
                    .error("Invalid token")
                    .status(HttpStatus.UNAUTHORIZED.value())
                    .timestamp(LocalDateTime.now())
                    .path("/api/auth/me")
                    .build();
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(error);
        }
    }

    @PostMapping("/validate")
    public ResponseEntity<?> validateToken(
            @RequestHeader("Authorization") String authHeader
    ) {
        try {
            String token = authHeader.substring(7);
            String userEmail = service.getUserEmailFromToken(token);
            var user = service.getUserByEmail(userEmail);
            
            if (jwtService.isTokenValid(token, user)) {
                return ResponseEntity.ok().body(Map.of("valid", true, "user", userEmail));
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("valid", false));
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("valid", false));
        }
    }
} 