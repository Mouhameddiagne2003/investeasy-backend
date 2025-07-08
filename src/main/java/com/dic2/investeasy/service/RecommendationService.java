package com.dic2.investeasy.service;

import com.dic2.investeasy.domain.Recommendation;
import com.dic2.investeasy.domain.User;
import com.dic2.investeasy.dto.recommendation.RecommendationRequestDTO;
import com.dic2.investeasy.dto.recommendation.RecommendationResponseDTO;
import com.dic2.investeasy.repository.RecommendationRepository;
import com.dic2.investeasy.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RecommendationService {
    private final RecommendationRepository recommendationRepository;
    private final UserRepository userRepository;

    public Page<Recommendation> getUserRecommendations(String userId, Pageable pageable) {
        return recommendationRepository.findByUserId(userId, pageable);
    }

    public Recommendation getRecommendationById(String id) {
        return recommendationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Recommendation not found"));
    }

    @Transactional
    public RecommendationResponseDTO createRecommendation(String userId, RecommendationRequestDTO dto) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Recommendation recommendation = new Recommendation();
        recommendation.setUser(user);
        recommendation.setBudget(dto.getBudget());
        recommendation.setGoal(dto.getGoal());
        recommendation.setRiskTolerance(dto.getRiskTolerance());
        recommendation.setTimeHorizon(dto.getTimeHorizon());
        recommendation.setAge(dto.getAge());
        recommendation.setCurrentInvestments(dto.getCurrentInvestments());

        // Calculer la recommandation en fonction des paramètres
        calculateRecommendation(recommendation);

        recommendation = recommendationRepository.save(recommendation);
        return convertToResponseDTO(recommendation);
    }

    private void calculateRecommendation(Recommendation recommendation) {
        // Logique de calcul de la recommandation
        // Cette partie devrait être implémentée selon les règles métier spécifiques
        recommendation.setRiskLevel(determineRiskLevel(recommendation));
        recommendation.setExpectedReturn(calculateExpectedReturn(recommendation));
        recommendation.setTimeframe(determineTimeframe(recommendation));
        recommendation.setNextSteps(generateNextSteps(recommendation));
    }

    private String determineRiskLevel(Recommendation recommendation) {
        // Logique pour déterminer le niveau de risque
        return recommendation.getRiskTolerance().toString();
    }

    private Double calculateExpectedReturn(Recommendation recommendation) {
        // Logique pour calculer le rendement attendu
        return 0.0; // À implémenter
    }

    private String determineTimeframe(Recommendation recommendation) {
        // Logique pour déterminer l'horizon temporel
        return recommendation.getTimeHorizon().toString();
    }

    private List<String> generateNextSteps(Recommendation recommendation) {
        // Logique pour générer les prochaines étapes
        List<String> steps = new ArrayList<>();
        steps.add("Étape 1: Analyser votre situation financière actuelle");
        steps.add("Étape 2: Définir vos objectifs d'investissement");
        steps.add("Étape 3: Évaluer votre tolérance au risque");
        return steps;
    }

    private RecommendationResponseDTO convertToResponseDTO(Recommendation recommendation) {
        RecommendationResponseDTO dto = new RecommendationResponseDTO();
        dto.setId(recommendation.getId());
        dto.setUserId(recommendation.getUser().getId());
        dto.setCreatedAt(recommendation.getCreatedAt());

        RecommendationResponseDTO.RecommendationDetails details = new RecommendationResponseDTO.RecommendationDetails();
        details.setRiskLevel(recommendation.getRiskLevel());
        details.setExpectedReturn(recommendation.getExpectedReturn());
        details.setTimeframe(recommendation.getTimeframe());
        details.setNextSteps(recommendation.getNextSteps());

        dto.setRecommendation(details);
        return dto;
    }
} 