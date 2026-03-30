package com.ftn.activityapp.mapper;

import com.ftn.activityapp.ai.category.nutrition.MealSuggestion;
import com.ftn.activityapp.ai.category.nutrition.NutritionRecommendation;
import com.ftn.activityapp.dto.nutrition.MealSuggestionDto;
import com.ftn.activityapp.dto.nutrition.NutritionResponseDto;
import com.ftn.activityapp.model.nutrition.MealSuggestionEntity;
import com.ftn.activityapp.model.nutrition.NutritionRecommendationEntity;

import java.util.Comparator;
import java.util.List;

public class NutritionMapper {

    public static NutritionRecommendationEntity toEntity(NutritionRecommendation nutrition) {
        if (nutrition == null) {
            return null;
        }

        NutritionRecommendationEntity entity = new NutritionRecommendationEntity();
        entity.setNutritionStatus(nutrition.getNutritionStatus());
        entity.setWaterIntakeTip(nutrition.getWaterIntakeTip());
        entity.setNutritionTip(nutrition.getNutritionTip());

        if (nutrition.getMeals() != null) {
            for (MealSuggestion meal : nutrition.getMeals()) {
                MealSuggestionEntity mealEntity = new MealSuggestionEntity();
                mealEntity.setMealType(meal.getMealType());
                mealEntity.setTitle(meal.getTitle());
                mealEntity.setDescription(meal.getDescription());
                mealEntity.setImageKey(meal.getImageKey());
                mealEntity.setSortOrder(meal.getSortOrder());
                mealEntity.setCalories(meal.getCalories());
                mealEntity.setRecipe(meal.getRecipe());
                entity.addMeal(mealEntity);
            }
        }

        return entity;
    }

    public static NutritionResponseDto toDto(NutritionRecommendationEntity entity) {
        if (entity == null) {
            return null;
        }

        List<MealSuggestionDto> mealDtos = entity.getMeals().stream()
                .sorted(Comparator.comparing(
                        meal -> meal.getSortOrder() != null ? meal.getSortOrder() : Integer.MAX_VALUE
                ))
                .map(meal -> MealSuggestionDto.builder()
                        .mealType(meal.getMealType())
                        .title(meal.getTitle())
                        .description(meal.getDescription())
                        .imageKey(meal.getImageKey())
                        .sortOrder(meal.getSortOrder())
                        .calories(meal.getCalories())
                        .recipe(meal.getRecipe())
                        .build())
                .toList();

        return NutritionResponseDto.builder()
                .nutritionStatus(entity.getNutritionStatus())
                .waterIntakeTip(entity.getWaterIntakeTip())
                .nutritionTip(entity.getNutritionTip())
                .meals(mealDtos)
                .build();
    }
}