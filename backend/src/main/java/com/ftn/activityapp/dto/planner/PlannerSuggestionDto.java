package com.ftn.activityapp.dto.planner;

import com.ftn.activityapp.enums.PlannerSuggestionType;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PlannerSuggestionDto {
    private String title;
    private String description;
    private Integer suggestedDurationMinutes;
    private PlannerSuggestionType suggestionType;
    private String recommendedPartOfDay;
    private String taskType;
    private String source;
}
