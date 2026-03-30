package com.ftn.activityapp.ai;

import com.ftn.activityapp.ai.category.wellness.WellnessRecommendation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
public class AiClient {

    private final RestTemplate restTemplate;

    private static final String AI_BASE_URL = "http://localhost:8000";
    private static final String RECOMMEND_URL = AI_BASE_URL + "/recommend";

    public AiRecommendationResponse getRecommendation(AiRecommendationRequest request) {
        ResponseEntity<AiRecommendationResponse> response =
                restTemplate.postForEntity(RECOMMEND_URL, request, AiRecommendationResponse.class);

        return response.getBody();
    }

    public WellnessRecommendation getWellnessByMood(String mood) {
        String url = AI_BASE_URL + "/wellness/" + mood;
        return restTemplate.getForObject(url, WellnessRecommendation.class);
    }
}