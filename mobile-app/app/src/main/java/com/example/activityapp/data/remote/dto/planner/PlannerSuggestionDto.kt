package com.example.activityapp.data.remote.dto.planner

data class PlannerSuggestionDto(
    val title: String,
    val description: String,
    val suggestedDurationMinutes: Int,
    val suggestionType: String,
    val recommendedPartOfDay: String,
    val taskType: String,
    val source: String
)