package com.ftn.activityapp.ai;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AiRecommendationRequest {

    private int stepsToday;
    private double avgLast7Days;
    private int daysGoalReachedLast7;
    private int goalSteps;
}
