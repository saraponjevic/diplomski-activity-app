package com.ftn.activityapp.service;

import com.ftn.activityapp.dto.RecommendationResponse;
import com.ftn.activityapp.enums.IntensityLevel;
import com.ftn.activityapp.enums.RecommendationType;
import com.ftn.activityapp.exception.ResourceNotFoundException;
import com.ftn.activityapp.model.Activity;
import com.ftn.activityapp.model.Recommendation;
import com.ftn.activityapp.model.User;
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

    /*
    * nadje korsnika
    * uzme aktivnost u poslednjih 7 dana
    * izracuna stepsToday i avgLast7Days
    * na osnovu toga pravi preporuku
    * sacuva preporuku u bazu
    *  */
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

        RecommendationType recommendationType;
        IntensityLevel intensity;
        int durationMinutes;
        String message;

        if (stepsToday < 3000) {
            recommendationType = RecommendationType.LIGHT_WALK;
            intensity = IntensityLevel.LOW;
            durationMinutes = 30;
            message = "You were not very active today. A light 30-minute walk is recommended.";
        } else if (stepsToday < 7000) {
            recommendationType = RecommendationType.MODERATE_WALK;
            intensity = IntensityLevel.MEDIUM;
            durationMinutes = 40;
            message = "Your activity level is moderate today. A 40-minute moderate walk is recommended.";
        } else if (avgLast7Days > 9000) {
            recommendationType = RecommendationType.RECOVERY_DAY;
            intensity = IntensityLevel.LOW;
            durationMinutes = 20;
            message = "You have been very active recently. A lighter recovery day is recommended.";
        } else {
            recommendationType = RecommendationType.HOME_WORKOUT;
            intensity = IntensityLevel.MEDIUM;
            durationMinutes = 25;
            message = "You are doing well. A short home workout is recommended to maintain consistency.";
        }

        Recommendation recommendation = Recommendation.builder()
                .user(user)
                .date(today)
                .recommendationType(recommendationType)
                .intensity(intensity)
                .durationMinutes(durationMinutes)
                .message(message)
                .build();

        Recommendation savedRecommendation = recommendationRepository.save(recommendation);

        return RecommendationResponse.builder()
                .id(savedRecommendation.getId())
                .userId(savedRecommendation.getUser().getId())
                .date(savedRecommendation.getDate())
                .recommendationType(savedRecommendation.getRecommendationType())
                .intensity(savedRecommendation.getIntensity())
                .durationMinutes(savedRecommendation.getDurationMinutes())
                .message(savedRecommendation.getMessage())
                .build();
    }

    public RecommendationResponse getLatestRecommendation(Long userId) {
        Recommendation recommendation = recommendationRepository.findTopByUserIdOrderByDateDesc(userId)
                .orElseThrow(() -> new ResourceNotFoundException("No recommendation found for user id: " + userId));

        return RecommendationResponse.builder()
                .id(recommendation.getId())
                .userId(recommendation.getUser().getId())
                .date(recommendation.getDate())
                .recommendationType(recommendation.getRecommendationType())
                .intensity(recommendation.getIntensity())
                .durationMinutes(recommendation.getDurationMinutes())
                .message(recommendation.getMessage())
                .build();
    }
}
