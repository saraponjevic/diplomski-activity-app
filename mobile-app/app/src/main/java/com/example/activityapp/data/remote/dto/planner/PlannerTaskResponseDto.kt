package com.example.activityapp.data.remote.dto.planner

data class PlannerTaskResponseDto(
    val id: Long,
    val title: String,
    val description: String?,
    val date: String,
    val time: String,
    val taskType: String,
    val completed: Boolean,
    val source: String,
    val userId: Long
)