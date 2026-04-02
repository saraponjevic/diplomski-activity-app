package com.ftn.activityapp.service;

import com.ftn.activityapp.ai.AiClient;
import com.ftn.activityapp.ai.AiRecommendationRequest;
import com.ftn.activityapp.ai.AiRecommendationResponse;
import com.ftn.activityapp.dto.MotivationResponseDto;
import com.ftn.activityapp.dto.RecommendationResponse;
import com.ftn.activityapp.dto.freetime.FreeTimeActivityDto;
import com.ftn.activityapp.dto.freetime.FreeTimeCategoryGroupDto;
import com.ftn.activityapp.dto.freetime.FreeTimeResponseDto;
import com.ftn.activityapp.dto.planner.PlannerSuggestionDto;
import com.ftn.activityapp.enums.FreeTimeCategory;
import com.ftn.activityapp.enums.IntensityLevel;
import com.ftn.activityapp.enums.RecommendationType;
import com.ftn.activityapp.exception.ResourceNotFoundException;
import com.ftn.activityapp.mapper.NutritionMapper;
import com.ftn.activityapp.model.Activity;
import com.ftn.activityapp.model.Recommendation;
import com.ftn.activityapp.model.User;
import com.ftn.activityapp.model.freetime.FreeTimeActivityEntity;
import com.ftn.activityapp.model.nutrition.NutritionRecommendationEntity;
import com.ftn.activityapp.model.planner.PlannerSuggestionEntity;
import com.ftn.activityapp.repository.ActivityRepository;
import com.ftn.activityapp.repository.RecommendationRepository;
import com.ftn.activityapp.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
                .freeTimeMainSuggestion(aiResponse.getFreeTime().getMainSuggestion())
                .freeTimeHeadline(aiResponse.getFreeTime().getHeadline())
                .motivationMessage(aiResponse.getMotivation().getMessage())
                .build();


        List<PlannerSuggestionEntity> plannerSuggestionEntities = new ArrayList<>();

        if (aiResponse.getPlannerSuggestions() != null) {
            aiResponse.getPlannerSuggestions().forEach(suggestion -> {
                PlannerSuggestionEntity entity = PlannerSuggestionEntity.builder()
                        .title(suggestion.getTitle())
                        .description(suggestion.getDescription())
                        .suggestedDurationMinutes(suggestion.getSuggestedDurationMinutes())
                        .suggestionType(suggestion.getSuggestionType())
                        .recommendedPartOfDay(suggestion.getRecommendedPartOfDay())
                        .taskType(suggestion.getTaskType())
                        .source(suggestion.getSource())
                        .recommendation(recommendation)
                        .build();

                plannerSuggestionEntities.add(entity);
            });
        }

        recommendation.setPlannerSuggestions(plannerSuggestionEntities);

        List<FreeTimeActivityEntity> freeTimeActivities = new ArrayList<>();

        aiResponse.getFreeTime().getCategoryGroups().forEach(group -> {
            group.getActivities().forEach(activity -> {
                FreeTimeActivityEntity entity = FreeTimeActivityEntity.builder()
                        .category(activity.getCategory())
                        .title(activity.getTitle())
                        .description(activity.getDescription())
                        .durationMinutes(activity.getDurationMinutes())
                        .intensity(activity.getIntensity())
                        .imageKey(mapCategoryToImageKey(activity.getCategory()))
                        .sortOrder(activity.getSortOrder())
                        .recommendation(recommendation)
                        .build();

                freeTimeActivities.add(entity);
            });
        });

        recommendation.setFreeTimeActivities(freeTimeActivities);

        Recommendation savedRecommendation = recommendationRepository.save(recommendation);

        return mapToResponse(savedRecommendation);
    }

    public RecommendationResponse getLatestRecommendation(Long userId) {
        Recommendation recommendation = recommendationRepository.findTopByUserIdOrderByDateDescIdDesc(userId)
                .orElseThrow(() -> new ResourceNotFoundException("No recommendation found for user id: " + userId));

        return mapToResponse(recommendation);
    }

    private List<PlannerSuggestionDto> mapPlannerSuggestions(List<PlannerSuggestionEntity> suggestions) {
        if (suggestions == null) {
            return List.of();
        }

        return suggestions.stream()
                .map(s -> PlannerSuggestionDto.builder()
                        .title(s.getTitle())
                        .description(s.getDescription())
                        .suggestedDurationMinutes(s.getSuggestedDurationMinutes())
                        .suggestionType(s.getSuggestionType())
                        .recommendedPartOfDay(s.getRecommendedPartOfDay())
                        .taskType(s.getTaskType())
                        .source(s.getSource())
                        .build())
                .toList();
    }

    private RecommendationResponse mapToResponse(Recommendation recommendation) {
        Map<FreeTimeCategory, List<FreeTimeActivityEntity>> groupedActivities =
                recommendation.getFreeTimeActivities().stream()
                        .collect(Collectors.groupingBy(FreeTimeActivityEntity::getCategory));

        List<FreeTimeCategoryGroupDto> freeTimeGroups = groupedActivities.entrySet().stream()
                .sorted(Map.Entry.comparingByKey())
                .map(entry -> FreeTimeCategoryGroupDto.builder()
                        .category(entry.getKey())
                        .activities(
                                entry.getValue().stream()
                                        .sorted(Comparator.comparing(FreeTimeActivityEntity::getSortOrder))
                                        .map(activity -> FreeTimeActivityDto.builder()
                                                .title(activity.getTitle())
                                                .description(activity.getDescription())
                                                .category(activity.getCategory())
                                                .durationMinutes(activity.getDurationMinutes())
                                                .intensity(activity.getIntensity())
                                                .imageKey(activity.getImageKey())
                                                .sortOrder(activity.getSortOrder())
                                                .build())
                                        .toList()
                        )
                        .build())
                .toList();

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
                .freeTime(
                        FreeTimeResponseDto.builder()
                                .mainSuggestion(recommendation.getFreeTimeMainSuggestion())
                                .headline(recommendation.getFreeTimeHeadline())
                                .categoryGroups(freeTimeGroups)
                                .build()
                )
                .motivation(MotivationResponseDto.builder()
                        .message(recommendation.getMotivationMessage())
                        .build())
                .plannerSuggestions(
                        mapPlannerSuggestions(recommendation.getPlannerSuggestions())
                )
                .build();
    }

    private String mapCategoryToImageKey(FreeTimeCategory category) {
        return switch (category) {
            case ACTIVE -> "free_category_active";
            case CREATIVE -> "free_category_creative";
            case RELAX -> "free_category_relax";
            case SOCIAL -> "free_category_social";
            case LEARNING -> "free_category_learning";
            case PRODUCTIVE -> "free_category_productive";
            case OUTDOOR -> "free_category_outdoor";
            case MINDFULNESS -> "free_category_mindfulness";
        };
    }


}