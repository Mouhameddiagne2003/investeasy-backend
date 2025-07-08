package com.dic2.investeasy.domain;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Entity
@Table(name = "recommendations")
public class Recommendation {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false)
    private Double budget;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private InvestmentGoal goal;

    @Enumerated(EnumType.STRING)
    @Column(name = "risk_tolerance", nullable = false)
    private RiskTolerance riskTolerance;

    @Enumerated(EnumType.STRING)
    @Column(name = "time_horizon", nullable = false)
    private TimeHorizon timeHorizon;

    @Column(nullable = false)
    private Integer age;

    @Column(name = "current_investments", columnDefinition = "TEXT")
    private String currentInvestments;

    @Column(name = "risk_level", nullable = false)
    private String riskLevel;

    @Column(name = "expected_return", nullable = false)
    private Double expectedReturn;

    @Column(nullable = false)
    private String timeframe;

    @ElementCollection
    @CollectionTable(name = "recommendation_next_steps", joinColumns = @JoinColumn(name = "recommendation_id"))
    @Column(name = "step", columnDefinition = "TEXT")
    private List<String> nextSteps;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }
} 