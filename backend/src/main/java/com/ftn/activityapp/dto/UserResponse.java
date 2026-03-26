package com.ftn.activityapp.dto;

import com.ftn.activityapp.enums.ActivityLevel;
import com.ftn.activityapp.enums.UserGoal;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class UserResponse {
    private Long id;
    private String email;
    private String firstName;
    private String lastName;
    private Integer age;
    private Double height;
    private Double weight;
    private ActivityLevel activityLevel;
    private UserGoal goal;
}
