package com.dic2.investeasy.controller;

import com.dic2.investeasy.domain.Recommendation;
import com.dic2.investeasy.dto.recommendation.RecommendationRequestDTO;
import com.dic2.investeasy.dto.recommendation.RecommendationResponseDTO;
import com.dic2.investeasy.service.RecommendationService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/recommendations")
@RequiredArgsConstructor
public class RecommendationController {
    private final RecommendationService recommendationService;

    @GetMapping
    public ResponseEntity<Page<Recommendation>> getUserRecommendations(
            @AuthenticationPrincipal UserDetails userDetails,
            Pageable pageable) {
        return ResponseEntity.ok(recommendationService.getUserRecommendations(userDetails.getUsername(), pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Recommendation> getRecommendationById(@PathVariable String id) {
        return ResponseEntity.ok(recommendationService.getRecommendationById(id));
    }

    @PostMapping
    public ResponseEntity<RecommendationResponseDTO> createRecommendation(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestBody RecommendationRequestDTO dto) {
        return ResponseEntity.ok(recommendationService.createRecommendation(userDetails.getUsername(), dto));
    }
} 