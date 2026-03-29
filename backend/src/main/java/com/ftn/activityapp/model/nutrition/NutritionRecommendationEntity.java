package com.ftn.activityapp.model.nutrition;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "nutrition_recommendations")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class NutritionRecommendationEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nutritionStatus;
    private String waterIntakeTip;
    private String nutritionTip;

    @OneToMany(mappedBy = "nutritionRecommendation", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MealSuggestionEntity> meals = new ArrayList<>();

    public void addMeal(MealSuggestionEntity meal) {
        meals.add(meal);
        meal.setNutritionRecommendation(this);
    }

    public void removeMeal(MealSuggestionEntity meal) {
        meals.remove(meal);
        meal.setNutritionRecommendation(null);
    }
}
