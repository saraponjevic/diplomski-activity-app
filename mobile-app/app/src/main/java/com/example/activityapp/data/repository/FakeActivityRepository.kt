package com.example.activityapp.data.repository

import com.example.activityapp.data.model.DailyActivity
import com.example.activityapp.data.model.Recommendation
import com.example.activityapp.data.model.UserProfile
import com.example.activityapp.data.model.WeeklyStats

class FakeActivityRepository {

    fun getDailyActivity(): DailyActivity {
        return DailyActivity(
            stepsToday = 6240,
            dailyGoal = 8000,
            goalPercentage = 78.0,
            activityLevel = "Moderate",
            shortRecommendation = "Short evening walk would be useful."
        )
    }

    fun getWeeklyStats(): WeeklyStats {
        return WeeklyStats(
            dailySteps = listOf(6500, 7200, 8100, 5600, 9400, 7000, 6240),
            averageSteps = 7148.0,
            goalReachedDays = 2,
            trend = "Slight improvement"
        )
    }

    fun getRecommendation(): Recommendation {
        return Recommendation(
            activityType = "Light walk",
            durationMinutes = 25,
            intensity = "Low",
            nextDayGoal = 8500,
            advice = "Drink more water and stretch for 5 minutes."
        )
    }

    fun getUserProfile(): UserProfile {
        return UserProfile(
            fullName = "Sara Ponjević",
            email = "sara@example.com",
            age = 23,
            heightCm = 168,
            weightKg = 58,
            dailyGoal = 8000
        )
    }
}