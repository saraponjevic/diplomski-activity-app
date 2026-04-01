package com.ftn.activityapp.controller;

import com.ftn.activityapp.dto.planner.PlannerTaskCreateRequestDto;
import com.ftn.activityapp.dto.planner.PlannerTaskResponseDto;
import com.ftn.activityapp.dto.planner.PlannerTaskUpdateRequestDto;
import com.ftn.activityapp.service.planner.PlannerTaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/planner")
@RequiredArgsConstructor
@CrossOrigin
public class PlannerTaskController {

    private final PlannerTaskService plannerTaskService;

    @PostMapping("/users/{userId}/tasks")
    public PlannerTaskResponseDto createTask(
            @PathVariable Long userId,
            @RequestBody PlannerTaskCreateRequestDto request
    ) {
        return plannerTaskService.createTask(userId, request);
    }

    @GetMapping("/users/{userId}/tasks/today")
    public List<PlannerTaskResponseDto> getTodayTasks(@PathVariable Long userId) {
        return plannerTaskService.getTasksForToday(userId);
    }

    @GetMapping("/users/{userId}/tasks/by-date")
    public List<PlannerTaskResponseDto> getTasksForDate(
            @PathVariable Long userId,
            @RequestParam LocalDate date
    ) {
        return plannerTaskService.getTasksForDate(userId, date);
    }

    @GetMapping("/users/{userId}/tasks/week")
    public List<PlannerTaskResponseDto> getTasksForWeek(
            @PathVariable Long userId,
            @RequestParam LocalDate startDate,
            @RequestParam LocalDate endDate
    ) {
        return plannerTaskService.getTasksForWeek(userId, startDate, endDate);
    }

    @PutMapping("/tasks/{taskId}")
    public PlannerTaskResponseDto updateTask(
            @PathVariable Long taskId,
            @RequestBody PlannerTaskUpdateRequestDto request
    ) {
        return plannerTaskService.updateTask(taskId, request);
    }

    @PatchMapping("/tasks/{taskId}/completed")
    public PlannerTaskResponseDto markCompleted(
            @PathVariable Long taskId,
            @RequestParam Boolean completed
    ) {
        return plannerTaskService.markCompleted(taskId, completed);
    }

    @DeleteMapping("/tasks/{taskId}")
    public void deleteTask(@PathVariable Long taskId) {
        plannerTaskService.deleteTask(taskId);
    }
}
