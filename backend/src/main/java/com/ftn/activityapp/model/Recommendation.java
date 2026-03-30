package com.ftn.activityapp.model;

import com.ftn.activityapp.enums.DailyState;
import com.ftn.activityapp.enums.IntensityLevel;
import com.ftn.activityapp.enums.RecommendationType;
import com.ftn.activityapp.model.nutrition.NutritionRecommendationEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "recommendations")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Recommendation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "user_id")
    private User user;

    @Column(nullable = false)
    private LocalDate date;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private DailyState dailyState;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RecommendationType recommendationType;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private IntensityLevel intensity;

    @Column(nullable = false)
    private Integer durationMinutes;

    @Column(nullable = false, length = 500)
    private String message;

    @Column(length = 500)
    private String notification;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @JoinColumn(name = "nutrition_recommendation_id")
    private NutritionRecommendationEntity nutritionRecommendation;

    @Column(length = 500)
    private String freeTimeMainSuggestion;

    @Column(length = 1000)
    private String freeTimeHeadline;

    @OneToMany(mappedBy = "recommendation", cascade = CascadeType.ALL, orphanRemoval = true)
    private java.util.List<com.ftn.activityapp.model.freetime.FreeTimeActivityEntity> freeTimeActivities;

    @Column(length = 500)
    private String motivationMessage;


}
