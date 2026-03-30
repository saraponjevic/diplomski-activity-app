package com.ftn.activityapp.repository.wellness;

import com.ftn.activityapp.model.wellness.WellnessRecommendationEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.Optional;

public interface WellnessRecommendationRepository extends JpaRepository<WellnessRecommendationEntity, Long> {
    Optional<WellnessRecommendationEntity> findByUserIdAndDate(Long userId, LocalDate date);
}