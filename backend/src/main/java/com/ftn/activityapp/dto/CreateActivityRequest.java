package com.ftn.activityapp.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class CreateActivityRequest {
    private Long userId;
    private LocalDate date;
    private Integer steps;
    private Integer goalSteps;
}
