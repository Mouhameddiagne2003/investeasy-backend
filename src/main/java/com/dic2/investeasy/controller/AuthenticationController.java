package com.dic2.investeasy.controller;

import com.dic2.investeasy.dto.auth.AuthenticationRequest;
import com.dic2.investeasy.dto.auth.AuthenticationResponse;
import com.dic2.investeasy.dto.auth.RegisterRequest;
import com.dic2.investeasy.service.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthenticationController {
    private final AuthenticationService authenticationService;

    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> register(
            @RequestBody RegisterRequest request
    ) {
        return ResponseEntity.ok(authenticationService.register(request));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> login(
            @RequestBody AuthenticationRequest request
    ) {
        return ResponseEntity.ok(authenticationService.authenticate(request));
    }

    @PostMapping("/logout")
    public ResponseEntity<AuthenticationResponse> logout() {
        return ResponseEntity.ok(authenticationService.logout());
    }

    @PostMapping("/refresh")
    public ResponseEntity<AuthenticationResponse> refreshToken() {
        return ResponseEntity.ok(authenticationService.refreshToken());
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<AuthenticationResponse> forgotPassword(
            @RequestBody AuthenticationRequest request
    ) {
        return ResponseEntity.ok(authenticationService.forgotPassword(request));
    }

    @PostMapping("/reset-password")
    public ResponseEntity<AuthenticationResponse> resetPassword(
            @RequestBody AuthenticationRequest request
    ) {
        return ResponseEntity.ok(authenticationService.resetPassword(request));
    }
} 