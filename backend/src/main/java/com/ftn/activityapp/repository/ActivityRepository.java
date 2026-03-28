package com.ftn.activityapp.repository;

import com.ftn.activityapp.model.Activity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface ActivityRepository extends JpaRepository<Activity, Long> {
    List<Activity> findByUserId(Long userId);

    //zbog prosecnog broja koraka koji korisnik ima u poslednjih 7 dana
    List<Activity> findByUserIdAndDateBetween(Long userId, LocalDate startDate, LocalDate endDate);

    Optional<Activity> findByUserIdAndDate(Long userId, LocalDate date);

    Optional<Activity> findTopByUserIdOrderByDateDescIdDesc(Long userId);
}
