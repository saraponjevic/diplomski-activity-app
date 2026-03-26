package com.ftn.activityapp.controller;

import com.ftn.activityapp.dto.RecommendationResponse;
import com.ftn.activityapp.service.RecommendationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/recommendations")
@RequiredArgsConstructor
public class RecommendationController {

    private final RecommendationService recommendationService;

    @PostMapping("/generate/{userId}")
    public ResponseEntity<RecommendationResponse> generateRecommendation(@PathVariable Long userId) {
        RecommendationResponse response = recommendationService.generateRecommendation(userId);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/user/{userId}/latest")
    public ResponseEntity<RecommendationResponse> getLatestRecommendation(@PathVariable Long userId) {
        RecommendationResponse response = recommendationService.getLatestRecommendation(userId);
        return ResponseEntity.ok(response);
    }
}
