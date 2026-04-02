package com.ftn.activityapp.model.planner;

import com.ftn.activityapp.enums.PlannerSuggestionType;
import com.ftn.activityapp.model.Recommendation;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PlannerSuggestionEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    @Column(length = 1000)
    private String description;

    private Integer suggestedDurationMinutes;

    @Enumerated(EnumType.STRING)
    private PlannerSuggestionType suggestionType;

    private String recommendedPartOfDay;

    private String taskType;

    private String source;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "recommendation_id")
    private Recommendation recommendation;
}
