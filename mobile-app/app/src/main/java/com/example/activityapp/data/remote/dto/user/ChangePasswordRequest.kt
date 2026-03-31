package com.example.activityapp.data.remote.dto.user

data class ChangePasswordRequest(
    val currentPassword: String,
    val newPassword: String
)