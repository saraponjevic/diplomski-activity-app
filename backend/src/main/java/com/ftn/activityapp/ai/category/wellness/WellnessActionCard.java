package com.ftn.activityapp.ai.category.wellness;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class WellnessActionCard {
    private String imageKey;
    private String title;
    private String description;
    private Integer sortOrder;
}
