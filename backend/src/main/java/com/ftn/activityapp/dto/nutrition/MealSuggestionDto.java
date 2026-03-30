package com.ftn.activityapp.dto.nutrition;

import com.ftn.activityapp.enums.MealType;
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
public class MealSuggestionDto {
    private MealType mealType;
    private String title;
    private String description;
    private String imageKey;
    private Integer sortOrder;
    private Integer calories;
    private List<String> recipe;
}