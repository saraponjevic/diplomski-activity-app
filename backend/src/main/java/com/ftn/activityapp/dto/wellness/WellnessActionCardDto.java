package com.ftn.activityapp.dto.wellness;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class WellnessActionCardDto {
    private String imageKey;
    private String title;
    private String description;
    private Integer sortOrder;
}
