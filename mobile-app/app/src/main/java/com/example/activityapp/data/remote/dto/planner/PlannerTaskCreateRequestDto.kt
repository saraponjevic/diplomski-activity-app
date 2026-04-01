package com.example.activityapp.data.remote.dto.planner

data class PlannerTaskCreateRequestDto(
    val title: String,
    val description: String?,
    val date: String,
    val time: String,
    val taskType: String,
    val source: String = "MANUAL"
)