package com.ftn.activityapp.model.freetime;

import com.ftn.activityapp.enums.FreeTimeCategory;
import com.ftn.activityapp.model.Recommendation;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "free_time_activities")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FreeTimeActivityEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private FreeTimeCategory category;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false, length = 500)
    private String description;

    @Column(nullable = false)
    private Integer durationMinutes;

    @Column(nullable = false)
    private String intensity;

    @Column(nullable = false)
    private String imageKey;

    @Column(nullable = false)
    private Integer sortOrder;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "recommendation_id", nullable = false)
    private Recommendation recommendation;
}
