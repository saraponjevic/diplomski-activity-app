package com.ftn.activityapp.ai;

import com.ftn.activityapp.ai.category.freetime.FreeTimeRecommendation;
import com.ftn.activityapp.ai.category.MotivationRecommendation;
import com.ftn.activityapp.ai.category.nutrition.NutritionRecommendation;
import com.ftn.activityapp.ai.category.planner.PlannerSuggestion;
import com.ftn.activityapp.enums.DailyState;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AiRecommendationResponse {

    private DailyState dailyState;
    private String recommendationType;
    private String intensity;
    private int durationMinutes;
    private String message;
    private String notification;
    private NutritionRecommendation nutrition;
    private FreeTimeRecommendation freeTime;
    private MotivationRecommendation motivation;
    private List<PlannerSuggestion> plannerSuggestions;

}
