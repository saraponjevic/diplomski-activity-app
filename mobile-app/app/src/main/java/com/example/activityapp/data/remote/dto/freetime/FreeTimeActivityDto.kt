package com.example.activityapp.data.remote.dto.freetime

data class FreeTimeActivityDto(
    val title: String,
    val description: String,
    val category: String,
    val durationMinutes: Int,
    val intensity: String,
    val imageKey: String,
    val sortOrder: Int
)