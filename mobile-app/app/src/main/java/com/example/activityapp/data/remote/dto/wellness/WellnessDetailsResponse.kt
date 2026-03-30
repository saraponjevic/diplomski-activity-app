package com.example.activityapp.data.remote.dto.wellness

data class WellnessDetailsResponse(
    val mood: String?,
    val headline: String?,
    val wellnessTip: String?,
    val restTip: String?,
    val actionCards: List<WellnessActionCardResponse>?
)