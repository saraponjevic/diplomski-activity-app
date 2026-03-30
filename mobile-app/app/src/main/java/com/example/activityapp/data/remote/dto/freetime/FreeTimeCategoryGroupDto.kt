package com.example.activityapp.data.remote.dto.freetime

data class FreeTimeCategoryGroupDto(
    val category: String,
    val activities: List<FreeTimeActivityDto>
)