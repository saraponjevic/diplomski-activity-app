package com.ftn.activityapp.ai.category.nutrition;


import com.ftn.activityapp.enums.MealType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MealSuggestion {
    private MealType mealType;
    private String title;
    private String description;
    private String imageKey;
    private Integer sortOrder;
    private Integer calories;
    private List<String> recipe;
}