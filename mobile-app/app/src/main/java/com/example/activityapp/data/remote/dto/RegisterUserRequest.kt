package com.example.activityapp.data.remote.dto

data class RegisterUserRequest(
    val email: String,
    val password: String,
    val firstName: String,
    val lastName: String,
    val age: Int,
    val height: Double,
    val weight: Double,
    val activityLevel: String,
    val goal: String,
    val goalSteps: Int
)