package com.ftn.activityapp.service.planner;

import com.ftn.activityapp.dto.planner.PlannerTaskCreateRequestDto;
import com.ftn.activityapp.dto.planner.PlannerTaskResponseDto;
import com.ftn.activityapp.dto.planner.PlannerTaskUpdateRequestDto;
import com.ftn.activityapp.exception.ResourceNotFoundException;
import com.ftn.activityapp.model.User;
import com.ftn.activityapp.model.planner.PlannerTask;
import com.ftn.activityapp.repository.UserRepository;
import com.ftn.activityapp.repository.planner.PlannerTaskRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PlannerTaskService {

    private final PlannerTaskRepository plannerTaskRepository;
    private final UserRepository userRepository;

    public PlannerTaskResponseDto createTask(Long userId, PlannerTaskCreateRequestDto request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        PlannerTask task = PlannerTask.builder()
                .title(request.getTitle())
                .description(request.getDescription())
                .date(request.getDate())
                .time(request.getTime())
                .taskType(request.getTaskType())
                .completed(false)
                .source(request.getSource() != null ? request.getSource() : "MANUAL")
                .user(user)
                .build();

        return mapToDto(plannerTaskRepository.save(task));
    }

    public List<PlannerTaskResponseDto> getTasksForToday(Long userId) {
        return plannerTaskRepository.findByUser_IdAndDateOrderByTimeAsc(userId, LocalDate.now())
                .stream()
                .map(this::mapToDto)
                .toList();
    }

    public List<PlannerTaskResponseDto> getTasksForDate(Long userId, LocalDate date) {
        return plannerTaskRepository.findByUser_IdAndDateOrderByTimeAsc(userId, date)
                .stream()
                .map(this::mapToDto)
                .toList();
    }

    public List<PlannerTaskResponseDto> getTasksForWeek(Long userId, LocalDate startDate, LocalDate endDate) {
        return plannerTaskRepository.findByUser_IdAndDateBetweenOrderByDateAscTimeAsc(userId, startDate, endDate)
                .stream()
                .map(this::mapToDto)
                .toList();
    }

    public PlannerTaskResponseDto updateTask(Long taskId, PlannerTaskUpdateRequestDto request) {
        PlannerTask task = plannerTaskRepository.findById(taskId)
                .orElseThrow(() -> new ResourceNotFoundException("Planner task not found"));

        task.setTitle(request.getTitle());
        task.setDescription(request.getDescription());
        task.setDate(request.getDate());
        task.setTime(request.getTime());
        task.setTaskType(request.getTaskType());
        task.setCompleted(request.getCompleted());

        return mapToDto(plannerTaskRepository.save(task));
    }

    public PlannerTaskResponseDto markCompleted(Long taskId, Boolean completed) {
        PlannerTask task = plannerTaskRepository.findById(taskId)
                .orElseThrow(() -> new ResourceNotFoundException("Planner task not found"));

        task.setCompleted(completed);
        return mapToDto(plannerTaskRepository.save(task));
    }

    public void deleteTask(Long taskId) {
        PlannerTask task = plannerTaskRepository.findById(taskId)
                .orElseThrow(() -> new ResourceNotFoundException("Planner task not found"));

        plannerTaskRepository.delete(task);
    }

    private PlannerTaskResponseDto mapToDto(PlannerTask task) {
        return PlannerTaskResponseDto.builder()
                .id(task.getId())
                .title(task.getTitle())
                .description(task.getDescription())
                .date(task.getDate())
                .time(task.getTime())
                .taskType(task.getTaskType())
                .completed(task.getCompleted())
                .source(task.getSource())
                .userId(task.getUser().getId())
                .build();
    }
}
