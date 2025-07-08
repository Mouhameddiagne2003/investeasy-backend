package com.dic2.investeasy.controller;

import lombok.Data;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeController {

    @GetMapping("/")
    public ResponseEntity<HelloResponse> hello() {
        return ResponseEntity.ok(new HelloResponse("Bienvenue sur l'API InvestEasy!"));
    }

    @GetMapping("/health")
    public ResponseEntity<HealthResponse> health() {
        return ResponseEntity.ok(new HealthResponse("UP", "Application is running"));
    }

    @Data
    public static class HelloResponse {
        private final String message;
    }

    @Data
    public static class HealthResponse {
        private final String status;
        private final String message;
    }
} 