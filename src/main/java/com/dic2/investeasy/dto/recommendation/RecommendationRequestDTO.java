package com.dic2.investeasy.dto.recommendation;

import com.dic2.investeasy.domain.InvestmentGoal;
import com.dic2.investeasy.domain.RiskTolerance;
import com.dic2.investeasy.domain.TimeHorizon;

import lombok.Data;

@Data
public class RecommendationRequestDTO {

    private Double budget;

    private InvestmentGoal goal;

    private RiskTolerance riskTolerance;

    private TimeHorizon timeHorizon;

    private Integer age;

    private String currentInvestments;
} 