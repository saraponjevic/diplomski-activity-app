package com.example.activityapp.data.remote.dto.freetime

data class FreeTimeResponseDto(
    val mainSuggestion: String,
    val headline: String,
    val categoryGroups: List<FreeTimeCategoryGroupDto>
)