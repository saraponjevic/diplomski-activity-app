package com.ftn.activityapp.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;


//dnevna aktivnost korisnika - broj koraka
// čuva broj koraka za tekući dan i istoriju aktivnosti za prethodni period


@Entity
@Table(name = "activities")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Activity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "user_id")
    private User user;

    @Column(nullable = false)
    private LocalDate date;

    @Column(nullable = false)
    private Integer steps;

    @Column(nullable = false)
    private Integer goalSteps;
}
