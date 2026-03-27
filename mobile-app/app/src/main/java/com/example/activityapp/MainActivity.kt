package com.example.activityapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.*
import com.example.activityapp.ui.screens.DashboardScreen
import com.example.activityapp.ui.screens.LoginScreen
import com.example.activityapp.ui.screens.ProfileScreen
import com.example.activityapp.ui.screens.RecommendationsScreen
import com.example.activityapp.ui.screens.RegisterScreen
import com.example.activityapp.ui.screens.WeeklyStatsScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            var currentScreen by remember { mutableStateOf("login") }
            var currentUserId by remember { mutableStateOf<Long?>(null) }

            when (currentScreen) {
                "login" -> LoginScreen(
                    onLoginSuccess = { userId ->
                        currentUserId = userId
                        currentScreen = "dashboard"
                    },
                    onGoToRegisterClick = { currentScreen = "register" }
                )

                "register" -> RegisterScreen(
                    onRegisterSuccess = { userId ->
                        currentUserId = userId
                        currentScreen = "dashboard"
                    },
                    onBackToLoginClick = { currentScreen = "login" }
                )

                "dashboard" -> DashboardScreen(
                    onWeeklyStatsClick = { currentScreen = "weeklyStats" },
                    onRecommendationsClick = { currentScreen = "recommendations" },
                    onProfileClick = { currentScreen = "profile" },
                    onLogoutClick = {
                        currentUserId = null
                        currentScreen = "login"
                    }
                )

                "weeklyStats" -> {
                    if (currentUserId != null) {
                        WeeklyStatsScreen(
                            userId = currentUserId!!,
                            onBackClick = { currentScreen = "dashboard" }
                        )
                    }
                }

                "recommendations" -> {
                    if (currentUserId != null) {
                        RecommendationsScreen(
                            userId = currentUserId!!,
                            onBackClick = { currentScreen = "dashboard" }
                        )
                    }
                }

                "profile" -> {
                    if (currentUserId != null) {
                        ProfileScreen(
                            userId = currentUserId!!,
                            onBackClick = { currentScreen = "dashboard" }
                        )
                    }
                }
            }
        }
    }
}