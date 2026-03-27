package com.example.activityapp.data.remote.dto

data class CreateActivityRequest(
    val userId: Long,
    val date: String,
    val steps: Int,
    val goalSteps: Int
)