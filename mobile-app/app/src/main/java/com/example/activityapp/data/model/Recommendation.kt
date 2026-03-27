package com.example.activityapp.data.model

data class Recommendation(
    val activityType: String,
    val durationMinutes: Int,
    val intensity: String,
    val nextDayGoal: Int,
    val advice: String
)