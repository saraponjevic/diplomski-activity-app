package com.example.activityapp.data.model

data class WeeklyStats(
    val dailySteps: List<Int>,
    val averageSteps: Double,
    val goalReachedDays: Int,
    val trend: String
)