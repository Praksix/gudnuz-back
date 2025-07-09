package com.praksix.Gnudnuz.service;

import com.praksix.Gnudnuz.dto.AuthResponse;
import com.praksix.Gnudnuz.dto.LoginRequest;
import com.praksix.Gnudnuz.dto.RegisterRequest;
import com.praksix.Gnudnuz.dto.RefreshTokenRequest;
import com.praksix.Gnudnuz.model.User;
import com.praksix.Gnudnuz.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationService {
    @Autowired
    private UserRepository repository;
    
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    @Autowired
    private JwtService jwtService;
    
    @Autowired
    private AuthenticationManager authenticationManager;

    public AuthResponse register(RegisterRequest request) {
        // Validation des données d'entrée
        if (request.getEmail() == null || request.getEmail().trim().isEmpty()) {
            throw new RuntimeException("L'email est requis");
        }
        if (request.getUsername() == null || request.getUsername().trim().isEmpty()) {
            throw new RuntimeException("Le nom d'utilisateur est requis");
        }
        if (request.getPassword() == null || request.getPassword().length() < 6) {
            throw new RuntimeException("Le mot de passe doit contenir au moins 6 caractères");
        }
        
        // Vérifier si l'email existe déjà
        if (repository.existsByEmail(request.getEmail().toLowerCase().trim())) {
            throw new RuntimeException("Email déjà utilisé");
        }
        
        // Vérifier si le username existe déjà
        if (repository.existsByUsername(request.getUsername().trim())) {
            throw new RuntimeException("Nom d'utilisateur déjà utilisé");
        }

        var user = User.builder()
                .username(request.getUsername().trim())
                .email(request.getEmail().toLowerCase().trim())
                .password(passwordEncoder.encode(request.getPassword()))
                .role("USER")
                .status("ACTIVE")
                .build();
        repository.save(user);
        var jwtToken = jwtService.generateToken(user);
        var refreshToken = jwtService.generateRefreshToken(user);
        return AuthResponse.builder()
                .token(jwtToken)
                .refreshToken(refreshToken)
                .userId(user.getId())
                .username(user.getDisplayUsername())
                .email(user.getEmail())
                .expiresIn(86400000) // 24h
                .build();
    }

    public AuthResponse authenticate(LoginRequest request) {
        // Validation des données d'entrée
        if (request.getEmail() == null || request.getEmail().trim().isEmpty()) {
            throw new RuntimeException("L'email est requis");
        }
        if (request.getPassword() == null || request.getPassword().isEmpty()) {
            throw new RuntimeException("Le mot de passe est requis");
        }
        
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail().toLowerCase().trim(),
                        request.getPassword()
                )
        );
        var user = repository.findByEmail(request.getEmail().toLowerCase().trim())
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));
        var jwtToken = jwtService.generateToken(user);
        var refreshToken = jwtService.generateRefreshToken(user);
        return AuthResponse.builder()
                .token(jwtToken)
                .refreshToken(refreshToken)
                .userId(user.getId())
                .username(user.getDisplayUsername())
                .email(user.getEmail())
                .expiresIn(86400000) // 24h
                .build();
    }

    public AuthResponse refreshToken(RefreshTokenRequest request) {
        final String refreshToken = request.getRefreshToken();
        final String userEmail = jwtService.extractUsername(refreshToken);
        
        if (userEmail != null) {
            var user = repository.findByEmail(userEmail)
                    .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));
            
            if (jwtService.isTokenValid(refreshToken, user)) {
                var newAccessToken = jwtService.generateToken(user);
                var newRefreshToken = jwtService.generateRefreshToken(user);
                
                return AuthResponse.builder()
                        .token(newAccessToken)
                        .refreshToken(newRefreshToken)
                        .userId(user.getId())
                        .username(user.getDisplayUsername())
                        .email(user.getEmail())
                        .expiresIn(86400000) // 24h
                        .build();
            }
        }
        throw new RuntimeException("Token de rafraîchissement invalide");
    }

    public void logout(String userId) {
        // Optionnel : Ajouter le token à une liste noire
        // Pour l'instant, on se contente de valider que l'utilisateur existe
        if (!repository.existsById(userId)) {
            throw new RuntimeException("Utilisateur non trouvé");
        }
        // Logique de déconnexion (peut être étendue plus tard)
    }

    public String getUserEmailFromToken(String token) {
        return jwtService.extractUsername(token);
    }

    public User getUserByEmail(String email) {
        return repository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));
    }
} 