package com.ftn.activityapp.model.wellness;

import com.ftn.activityapp.enums.MoodType;
import com.ftn.activityapp.model.User;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "wellness_recommendations")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WellnessRecommendationEntity {

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
    private MoodType mood;

    @Column(nullable = false, length = 500)
    private String headline;

    @Column(nullable = false, length = 500)
    private String wellnessTip;

    @Column(nullable = false, length = 500)
    private String restTip;

    @OneToMany(mappedBy = "wellnessRecommendation", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("sortOrder ASC")
    @Builder.Default
    private List<WellnessActionCardEntity> actionCards = new ArrayList<>();
}
