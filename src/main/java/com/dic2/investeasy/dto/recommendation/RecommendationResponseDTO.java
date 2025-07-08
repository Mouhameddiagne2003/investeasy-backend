package com.dic2.investeasy.dto.recommendation;

import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class RecommendationResponseDTO {
    private String id;
    private String userId;
    private RecommendationDetails recommendation;
    private LocalDateTime createdAt;

    @Data
    public static class RecommendationDetails {
        private List<PortfolioItem> portfolio;
        private String riskLevel;
        private Double expectedReturn;
        private String timeframe;
        private List<String> nextSteps;
    }

    @Data
    public static class PortfolioItem {
        private String type;
        private Double allocation;
        private String description;
        private String reasoning;
    }
} 