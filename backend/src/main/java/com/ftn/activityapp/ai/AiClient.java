package com.ftn.activityapp.ai;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
public class AiClient {

    private final RestTemplate restTemplate;

    private static final String AI_URL = "http://localhost:8000/recommend";

    public AiRecommendationResponse getRecommendation(AiRecommendationRequest request) {

        ResponseEntity<AiRecommendationResponse> response =
                restTemplate.postForEntity(AI_URL, request, AiRecommendationResponse.class);

        return response.getBody();
    }
}
