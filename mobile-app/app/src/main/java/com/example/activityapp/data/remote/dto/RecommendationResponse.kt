package com.example.activityapp.data.remote.dto

import com.example.activityapp.data.remote.dto.freetime.FreeTimeResponseDto
import com.example.activityapp.data.remote.dto.nutrition.NutritionResponse
import com.example.activityapp.data.remote.dto.planner.PlannerSuggestionDto
import com.example.activityapp.data.remote.dto.wellness.WellnessResponse

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
    val freeTime: FreeTimeResponseDto?,
    val wellness: WellnessResponse?,
    val motivation: MotivationResponse?,
    val plannerSuggestions: List<PlannerSuggestionDto> = emptyList()

)