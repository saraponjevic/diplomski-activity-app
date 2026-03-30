package com.ftn.activityapp.controller;

import com.ftn.activityapp.dto.activity.ActivityResponse;
import com.ftn.activityapp.dto.activity.CreateActivityRequest;
import com.ftn.activityapp.service.ActivityService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/activities")
@RequiredArgsConstructor
public class ActivityController {

    private final ActivityService activityService;

    @PostMapping
    public ResponseEntity<ActivityResponse> createActivity(@RequestBody CreateActivityRequest request) {
        ActivityResponse response = activityService.createActivity(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<ActivityResponse>> getActivitiesByUser(@PathVariable Long userId) {
        List<ActivityResponse> response = activityService.getActivitiesByUser(userId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/user/{userId}/latest")
    public ResponseEntity<ActivityResponse> getLatestActivityByUser(@PathVariable Long userId) {
        ActivityResponse response = activityService.getLatestActivityByUser(userId);
        return ResponseEntity.ok(response);
    }
}
