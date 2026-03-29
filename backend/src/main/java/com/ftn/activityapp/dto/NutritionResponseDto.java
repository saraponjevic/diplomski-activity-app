package com.ftn.activityapp.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NutritionResponseDto {
    private String nutritionStatus;
    private String waterIntakeTip;
    private String nutritionTip;
    private List<MealSuggestionDto> meals;
}
