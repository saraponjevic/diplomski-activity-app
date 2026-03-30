package com.ftn.activityapp.dto.wellness;

import com.ftn.activityapp.enums.MoodType;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class WellnessMoodRequestDto {
    private MoodType mood;
}