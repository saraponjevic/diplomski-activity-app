package com.ftn.activityapp.service.wellness;

import com.ftn.activityapp.ai.AiClient;
import com.ftn.activityapp.ai.category.wellness.WellnessRecommendation;
import com.ftn.activityapp.dto.wellness.WellnessActionCardDto;
import com.ftn.activityapp.dto.wellness.WellnessDetailsResponseDto;
import com.ftn.activityapp.enums.MoodType;
import com.ftn.activityapp.exception.ResourceNotFoundException;
import com.ftn.activityapp.model.User;
import com.ftn.activityapp.model.wellness.WellnessActionCardEntity;
import com.ftn.activityapp.model.wellness.WellnessEntry;
import com.ftn.activityapp.model.wellness.WellnessRecommendationEntity;
import com.ftn.activityapp.repository.UserRepository;
import com.ftn.activityapp.repository.wellness.WellnessEntryRepository;
import com.ftn.activityapp.repository.wellness.WellnessRecommendationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class WellnessService {

    private final UserRepository userRepository;
    private final WellnessEntryRepository wellnessEntryRepository;
    private final WellnessRecommendationRepository wellnessRecommendationRepository;
    private final AiClient aiClient;

    public WellnessDetailsResponseDto saveMoodAndGenerateWellness(Long userId, MoodType mood) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));

        LocalDate today = LocalDate.now();

        if (wellnessEntryRepository.findByUserIdAndDate(userId, today).isPresent()) {
            throw new IllegalStateException("Mood has already been selected today.");
        }

        WellnessEntry entry = WellnessEntry.builder()
                .user(user)
                .date(today)
                .mood(mood)
                .build();

        wellnessEntryRepository.save(entry);

        WellnessRecommendation aiWellness = aiClient.getWellnessByMood(mood.name());

        if (aiWellness == null) {
            throw new ResourceNotFoundException("AI wellness response is empty.");
        }

        WellnessRecommendationEntity recommendation = WellnessRecommendationEntity.builder()
                .user(user)
                .date(today)
                .mood(mood)
                .headline(aiWellness.getHeadline())
                .wellnessTip(aiWellness.getWellnessTip())
                .restTip(aiWellness.getRestTip())
                .build();

        if (aiWellness.getActionCards() != null) {
            List<WellnessActionCardEntity> cards = aiWellness.getActionCards().stream()
                    .map(card -> WellnessActionCardEntity.builder()
                            .emoji(card.getEmoji())
                            .title(card.getTitle())
                            .description(card.getDescription())
                            .sortOrder(card.getSortOrder())
                            .wellnessRecommendation(recommendation)
                            .build())
                    .toList();

            recommendation.getActionCards().addAll(cards);
        }

        WellnessRecommendationEntity saved = wellnessRecommendationRepository.save(recommendation);

        return mapToDto(saved);
    }

    public WellnessDetailsResponseDto getTodayWellness(Long userId) {
        WellnessRecommendationEntity recommendation = wellnessRecommendationRepository
                .findByUserIdAndDate(userId, LocalDate.now())
                .orElseThrow(() -> new ResourceNotFoundException("No wellness recommendation found for today"));

        return mapToDto(recommendation);
    }

    private WellnessDetailsResponseDto mapToDto(WellnessRecommendationEntity entity) {
        return WellnessDetailsResponseDto.builder()
                .mood(entity.getMood())
                .headline(entity.getHeadline())
                .wellnessTip(entity.getWellnessTip())
                .restTip(entity.getRestTip())
                .actionCards(entity.getActionCards().stream()
                        .map(card -> WellnessActionCardDto.builder()
                                .emoji(card.getEmoji())
                                .title(card.getTitle())
                                .description(card.getDescription())
                                .sortOrder(card.getSortOrder())
                                .build())
                        .toList())
                .build();
    }
}