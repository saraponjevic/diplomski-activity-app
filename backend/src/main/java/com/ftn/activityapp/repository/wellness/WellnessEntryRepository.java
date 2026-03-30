package com.ftn.activityapp.repository.wellness;

import com.ftn.activityapp.model.wellness.WellnessEntry;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.Optional;

public interface WellnessEntryRepository extends JpaRepository<WellnessEntry, Long> {
    Optional<WellnessEntry> findByUserIdAndDate(Long userId, LocalDate date);
}
