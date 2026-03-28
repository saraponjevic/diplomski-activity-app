package com.ftn.activityapp.repository;

import com.ftn.activityapp.model.Recommendation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface RecommendationRepository extends JpaRepository<Recommendation, Long> {
    List<Recommendation> findByUserId(Long userId);

    Optional<Recommendation> findTopByUserIdOrderByDateDescIdDesc(Long userId);
}
