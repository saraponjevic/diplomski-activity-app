package com.ftn.activityapp.ai;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AiRecommendationResponse {

    private String recommendationType;
    private String intensity;
    private int durationMinutes;
    private String message;
}
