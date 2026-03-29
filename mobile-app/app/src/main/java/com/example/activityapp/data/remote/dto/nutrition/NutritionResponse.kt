package com.example.activityapp.data.remote.dto.nutrition

data class NutritionResponse(
    val nutritionStatus: String?,
    val waterIntakeTip: String?,
    val nutritionTip: String?,
    val meals: List<MealSuggestionResponse>?
)