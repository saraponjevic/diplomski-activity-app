package com.ftn.activityapp.model.wellness;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "wellness_action_cards")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WellnessActionCardEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false, length = 500)
    private String description;

    @Column(nullable = false)
    private String imageKey;

    @Column(nullable = false)
    private Integer sortOrder;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "wellness_recommendation_id")
    private WellnessRecommendationEntity wellnessRecommendation;
}
