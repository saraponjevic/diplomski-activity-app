package com.example.activityapp.data.model

data class DailyActivity(
    val stepsToday: Int,
    val dailyGoal: Int,
    val goalPercentage: Double,
    val activityLevel: String,
    val shortRecommendation: String
)