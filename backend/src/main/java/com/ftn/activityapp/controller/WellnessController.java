package com.ftn.activityapp.controller;

import com.ftn.activityapp.dto.wellness.WellnessDetailsResponseDto;
import com.ftn.activityapp.dto.wellness.WellnessMoodRequestDto;
import com.ftn.activityapp.service.wellness.WellnessService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/wellness")
@RequiredArgsConstructor
public class WellnessController {

    private final WellnessService wellnessService;

    @PostMapping("/{userId}/mood")
    public WellnessDetailsResponseDto saveMood(
            @PathVariable Long userId,
            @RequestBody WellnessMoodRequestDto request
    ) {
        return wellnessService.saveMoodAndGenerateWellness(userId, request.getMood());
    }

    @GetMapping("/{userId}/today")
    public WellnessDetailsResponseDto getTodayWellness(@PathVariable Long userId) {
        return wellnessService.getTodayWellness(userId);
    }
}