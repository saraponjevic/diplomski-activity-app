package com.ftn.activityapp.dto.wellness;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WellnessResponseDto {
    private String wellnessTip;
    private String restTip;
}