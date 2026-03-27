package com.example.activityapp.data.remote.dto

data class ActivityResponse(
    val id: Long,
    val userId: Long,
    val date: String,
    val steps: Int,
    val goalSteps: Int
)