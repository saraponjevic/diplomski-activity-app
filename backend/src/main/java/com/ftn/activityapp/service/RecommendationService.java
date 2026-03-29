package com.ftn.activityapp.service;

import com.ftn.activityapp.ai.AiClient;
import com.ftn.activityapp.ai.AiRecommendationRequest;
import com.ftn.activityapp.ai.AiRecommendationResponse;
import com.ftn.activityapp.dto.*;
import com.ftn.activityapp.enums.IntensityLevel;
import com.ftn.activityapp.enums.RecommendationType;
import com.ftn.activityapp.exception.ResourceNotFoundException;
import com.ftn.activityapp.mapper.NutritionMapper;
import com.ftn.activityapp.model.Activity;
import com.ftn.activityapp.model.Recommendation;
import com.ftn.activityapp.model.User;
import com.ftn.activityapp.model.nutrition.NutritionRecommendationEntity;
import com.ftn.activityapp.repository.ActivityRepository;
import com.ftn.activityapp.repository.RecommendationRepository;
import com.ftn.activityapp.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RecommendationService {

    private final RecommendationRepository recommendationRepository;
    private final ActivityRepository activityRepository;
    private final UserRepository userRepository;
    private final AiClient aiClient;

    public RecommendationResponse generateRecommendation(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));

        LocalDate today = LocalDate.now();
        LocalDate sevenDaysAgo = today.minusDays(6);

        List<Activity> last7DaysActivities =
                activityRepository.findByUserIdAndDateBetween(userId, sevenDaysAgo, today);

        int stepsToday = last7DaysActivities.stream()
                .filter(activity -> activity.getDate().equals(today))
                .map(Activity::getSteps)
                .findFirst()
                .orElse(0);

        double avgLast7Days = last7DaysActivities.stream()
                .mapToInt(Activity::getSteps)
                .average()
                .orElse(0);

        int daysGoalReachedLast7 = (int) last7DaysActivities.stream()
                .filter(activity -> activity.getSteps() >= activity.getGoalSteps())
                .count();

        int goalSteps = last7DaysActivities.stream()
                .filter(activity -> activity.getDate().equals(today))
                .map(Activity::getGoalSteps)
                .findFirst()
                .orElse(user.getGoalSteps());

        AiRecommendationRequest aiRequest = AiRecommendationRequest.builder()
                .stepsToday(stepsToday)
                .avgLast7Days(avgLast7Days)
                .daysGoalReachedLast7(daysGoalReachedLast7)
                .goalSteps(goalSteps)
                .build();

        AiRecommendationResponse aiResponse = aiClient.getRecommendation(aiRequest);

        NutritionRecommendationEntity nutritionEntity =
                NutritionMapper.toEntity(aiResponse.getNutrition());

        Recommendation recommendation = Recommendation.builder()
                .user(user)
                .date(today)
                .dailyState(aiResponse.getDailyState())
                .recommendationType(RecommendationType.valueOf(aiResponse.getRecommendationType()))
                .intensity(IntensityLevel.valueOf(aiResponse.getIntensity()))
                .durationMinutes(aiResponse.getDurationMinutes())
                .message(aiResponse.getMessage())
                .notification(aiResponse.getNotification())
                .nutritionRecommendation(nutritionEntity)
                .freeTimeSuggestion(aiResponse.getFreeTime().getActivitySuggestion())
                .wellnessTip(aiResponse.getWellness().getWellnessTip())
                .restTip(aiResponse.getWellness().getRestTip())
                .motivationMessage(aiResponse.getMotivation().getMessage())
                .build();

        Recommendation savedRecommendation = recommendationRepository.save(recommendation);

        return mapToResponse(savedRecommendation);
    }

    public RecommendationResponse getLatestRecommendation(Long userId) {
        Recommendation recommendation = recommendationRepository.findTopByUserIdOrderByDateDescIdDesc(userId)
                .orElseThrow(() -> new ResourceNotFoundException("No recommendation found for user id: " + userId));

        return mapToResponse(recommendation);
    }

    private RecommendationResponse mapToResponse(Recommendation recommendation) {
        return RecommendationResponse.builder()
                .id(recommendation.getId())
                .userId(recommendation.getUser().getId())
                .date(recommendation.getDate())
                .dailyState(recommendation.getDailyState())
                .recommendationType(recommendation.getRecommendationType())
                .intensity(recommendation.getIntensity())
                .durationMinutes(recommendation.getDurationMinutes())
                .message(recommendation.getMessage())
                .notification(recommendation.getNotification())
                .nutrition(NutritionMapper.toDto(recommendation.getNutritionRecommendation()))
                .freeTime(FreeTimeResponseDto.builder()
                        .activitySuggestion(recommendation.getFreeTimeSuggestion())
                        .build())
                .wellness(WellnessResponseDto.builder()
                        .wellnessTip(recommendation.getWellnessTip())
                        .restTip(recommendation.getRestTip())
                        .build())
                .motivation(MotivationResponseDto.builder()
                        .message(recommendation.getMotivationMessage())
                        .build())
                .build();
    }
}