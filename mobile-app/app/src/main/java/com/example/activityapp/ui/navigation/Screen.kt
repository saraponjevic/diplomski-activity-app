package com.example.activityapp.ui.navigation

sealed class Screen(val route: String, val title: String) {
    data object Dashboard : Screen("dashboard", "Dashboard")
    data object RecommendationDetails : Screen("recommendation_details", "Daily Recommendation")
    data object Nutrition : Screen("nutrition", "Nutrition")
    data object Wellness : Screen("wellness", "Wellness")
    data object FreeTime : Screen("free_time", "Free Time")
    data object Motivation : Screen("motivation", "Motivation")
    data object Profile : Screen("profile", "Profile")
    data object Login : Screen("login", "Login")

    data object WeeklyStats : Screen("weekly_stats", title= "Weekly status")
    data object Planner : Screen("planner", "Planner")
}