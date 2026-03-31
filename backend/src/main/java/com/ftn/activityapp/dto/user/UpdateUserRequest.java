package com.ftn.activityapp.dto.user;

import com.ftn.activityapp.enums.ActivityLevel;
import com.ftn.activityapp.enums.UserGoal;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateUserRequest {
    private String email;
    private String firstName;
    private String lastName;
    private Integer age;
    private Double height;
    private Double weight;
    private ActivityLevel activityLevel;
    private UserGoal goal;
    private Integer goalSteps;
}
