package com.ftn.activityapp.ai.category.nutrition;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class NutritionRecommendation {

    private String nutritionStatus;
    private String waterIntakeTip;
    private String nutritionTip;
    private List<MealSuggestion> meals;
}
