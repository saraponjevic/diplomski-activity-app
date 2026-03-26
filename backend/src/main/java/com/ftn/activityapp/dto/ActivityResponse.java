package com.ftn.activityapp.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@Builder
public class ActivityResponse {
    private Long id;
    private Long userId;
    private LocalDate date;
    private Integer steps;
    private Integer goalSteps;
}
