package com.example.activityapp.data.remote.dto.nutrition

data class MealSuggestionResponse(
    val mealType: String?,
    val title: String?,
    val description: String?,
    val imageKey: String?,
    val sortOrder: Int?,
    val calories: Int?,
    val recipe: List<String>?

)