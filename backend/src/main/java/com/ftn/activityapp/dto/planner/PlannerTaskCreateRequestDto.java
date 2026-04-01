package com.ftn.activityapp.dto.planner;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ftn.activityapp.enums.PlannerTaskType;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
public class PlannerTaskCreateRequestDto {
    private String title;
    private String description;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate date;

    @JsonFormat(pattern = "HH:mm")
    private LocalTime time;

    private PlannerTaskType taskType;
    private String source;
}
