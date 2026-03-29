package com.example.activityapp.data.remote.dto

import com.example.activityapp.data.remote.dto.nutrition.NutritionResponse

data class RecommendationResponse(
    val id: Long,
    val userId: Long,
    val date: String,
    val dailyState: String,
    val recommendationType: String,
    val intensity: String,
    val durationMinutes: Int,
    val message: String,
    val notification: String?,
    val nutrition: NutritionResponse?,
    val freeTime: FreeTimeResponse?,
    val wellness: WellnessResponse?,
    val motivation: MotivationResponse?

)