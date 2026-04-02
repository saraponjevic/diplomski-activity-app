package com.ftn.activityapp.repository.planner;

import com.ftn.activityapp.model.planner.PlannerTask;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface PlannerTaskRepository extends JpaRepository<PlannerTask, Long> {
    List<PlannerTask> findByUser_IdOrderByDateAscTimeAsc(Long userId);
    List<PlannerTask> findByUser_IdAndDateOrderByTimeAsc(Long userId, LocalDate date);
    List<PlannerTask> findByUser_IdAndDateBetweenOrderByDateAscTimeAsc(Long userId, LocalDate startDate, LocalDate endDate);
}