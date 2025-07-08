package com.dic2.investeasy.service;

import com.dic2.investeasy.domain.User;
import com.dic2.investeasy.domain.UserRole;
import com.dic2.investeasy.dto.auth.AuthenticationRequest;
import com.dic2.investeasy.dto.auth.AuthenticationResponse;
import com.dic2.investeasy.dto.auth.RegisterRequest;
import com.dic2.investeasy.repository.UserRepository;
import com.dic2.investeasy.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public AuthenticationResponse register(RegisterRequest request) {
        var user = User.builder()
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(UserRole.USER)
                .isActive(true)
                .build();
        
        userRepository.save(user);
        var jwtToken = jwtService.generateToken(user);
        
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .message("User registered successfully !")
                .user(user)
                .build();
    }

    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );
        
        var user = userRepository.findByEmail(request.getEmail())
                .orElseThrow();
        var jwtToken = jwtService.generateToken(user);
        
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .user(user)
                .build();
    }

    public AuthenticationResponse logout() {
        SecurityContextHolder.clearContext();
        return AuthenticationResponse.builder()
                .message("Logged out successfully")
                .build();
    }

    public AuthenticationResponse refreshToken() {
        var user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        var jwtToken = jwtService.generateToken(user);
        
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();
    }

    public AuthenticationResponse forgotPassword(AuthenticationRequest request) {
        var user = userRepository.findByEmail(request.getEmail())
                .orElseThrow();
        
        // TODO: Implement email sending logic
        return AuthenticationResponse.builder()
                .message("Reset email sent")
                .build();
    }

    public AuthenticationResponse resetPassword(AuthenticationRequest request) {
        var user = userRepository.findByEmail(request.getEmail())
                .orElseThrow();
        
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        userRepository.save(user);
        
        return AuthenticationResponse.builder()
                .message("Password reset successfully")
                .build();
    }
} 