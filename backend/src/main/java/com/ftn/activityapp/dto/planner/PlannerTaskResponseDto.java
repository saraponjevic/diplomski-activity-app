package com.ftn.activityapp.dto.planner;

import com.ftn.activityapp.enums.PlannerTaskType;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
@Builder
public class PlannerTaskResponseDto {
    private Long id;
    private String title;
    private String description;
    private LocalDate date;
    private LocalTime time;
    private PlannerTaskType taskType;
    private Boolean completed;
    private String source;
    private Long userId;
}
