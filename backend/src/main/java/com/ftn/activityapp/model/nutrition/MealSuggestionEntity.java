package com.ftn.activityapp.model.nutrition;

import com.ftn.activityapp.enums.MealType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "meal_suggestions")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MealSuggestionEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private MealType mealType;

    private String title;
    private String description;
    private String imageKey;
    private Integer sortOrder;

    private Integer calories;

    @ElementCollection
    @CollectionTable(name = "meal_recipe_steps", joinColumns = @JoinColumn(name = "meal_suggestion_id"))
    @Column(name = "recipe_step", length = 1000)
    private List<String> recipe = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "nutrition_recommendation_id")
    private NutritionRecommendationEntity nutritionRecommendation;
}
