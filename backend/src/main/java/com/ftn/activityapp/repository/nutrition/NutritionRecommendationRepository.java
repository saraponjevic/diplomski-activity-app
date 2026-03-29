package com.ftn.activityapp.repository.nutrition;

import com.ftn.activityapp.model.nutrition.NutritionRecommendationEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NutritionRecommendationRepository extends JpaRepository<NutritionRecommendationEntity, Long> {
}