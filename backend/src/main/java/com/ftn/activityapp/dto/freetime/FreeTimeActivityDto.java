package com.ftn.activityapp.dto.freetime;

import com.ftn.activityapp.enums.FreeTimeCategory;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FreeTimeActivityDto {
    private String title;
    private String description;
    private FreeTimeCategory category;
    private Integer durationMinutes;
    private String intensity;
    private String imageKey;
    private Integer sortOrder;
}
