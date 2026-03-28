package com.ftn.activityapp.dto;

import com.ftn.activityapp.enums.DailyState;
import com.ftn.activityapp.enums.IntensityLevel;
import com.ftn.activityapp.enums.RecommendationType;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@Builder
public class RecommendationResponse {
    private Long id;
    private Long userId;
    private LocalDate date;
    private DailyState dailyState;
    private RecommendationType recommendationType;
    private IntensityLevel intensity;
    private Integer durationMinutes;
    private String message;

    private String notification;

    private NutritionResponseDto nutrition;
    private FreeTimeResponseDto freeTime;
    private WellnessResponseDto wellness;
    private MotivationResponseDto motivation;
}