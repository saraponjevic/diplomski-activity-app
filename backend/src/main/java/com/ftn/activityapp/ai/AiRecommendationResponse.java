package com.ftn.activityapp.ai;

import com.ftn.activityapp.ai.category.FreeTimeRecommendation;
import com.ftn.activityapp.ai.category.MotivationRecommendation;
import com.ftn.activityapp.ai.category.nutrition.NutritionRecommendation;
import com.ftn.activityapp.ai.category.WellnessRecommendation;
import com.ftn.activityapp.enums.DailyState;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
    private WellnessRecommendation wellness;
    private MotivationRecommendation motivation;

}
