package com.ftn.activityapp.model.wellness;

import com.ftn.activityapp.enums.MoodType;
import com.ftn.activityapp.model.User;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "wellness_entries")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WellnessEntry {

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
}
