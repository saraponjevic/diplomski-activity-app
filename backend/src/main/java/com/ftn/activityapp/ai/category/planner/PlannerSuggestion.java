package com.ftn.activityapp.ai.category.planner;

import com.ftn.activityapp.enums.PlannerSuggestionType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PlannerSuggestion {
    private String title;
    private String description;
    private Integer suggestedDurationMinutes;
    private PlannerSuggestionType suggestionType;
    private String recommendedPartOfDay;
    private String taskType;
    private String source;
}
