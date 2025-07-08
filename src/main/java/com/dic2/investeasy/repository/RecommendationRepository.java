package com.dic2.investeasy.repository;

import com.dic2.investeasy.domain.Recommendation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RecommendationRepository extends JpaRepository<Recommendation, String> {
    Page<Recommendation> findByUserId(String userId, Pageable pageable);
} 