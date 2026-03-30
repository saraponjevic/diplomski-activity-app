package com.ftn.activityapp.dto.wellness;

import com.ftn.activityapp.enums.MoodType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class WellnessDetailsResponseDto {
    private MoodType mood;
    private String headline;
    private String wellnessTip;
    private String restTip;
    private List<WellnessActionCardDto> actionCards;
}
