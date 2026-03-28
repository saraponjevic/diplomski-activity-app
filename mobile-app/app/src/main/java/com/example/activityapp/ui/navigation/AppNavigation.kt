package com.example.activityapp.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.activityapp.ui.screens.DashboardScreen
import com.example.activityapp.ui.screens.FreeTimeScreen
import com.example.activityapp.ui.screens.MotivationScreen
import com.example.activityapp.ui.screens.NutritionScreen
import com.example.activityapp.ui.screens.ProfileScreen
import com.example.activityapp.ui.screens.RecommendationDetailsScreen
import com.example.activityapp.ui.screens.WellnessScreen

@Composable
fun AppNavigation(
    userId: Long,
    onLogoutClick: () -> Unit
) {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Screen.Dashboard.route
    ) {
        composable(Screen.Dashboard.route) {
            MainScaffoldScreen(
                title = "Dashboard",
                navController = navController,
                onLogoutClick = onLogoutClick
            ) {
                DashboardScreen(
                    userId = userId,
                    onRecommendationClick = {
                        navController.navigate(Screen.RecommendationDetails.route)
                    },
                    onNutritionClick = {
                        navController.navigate(Screen.Nutrition.route)
                    },
                    onWellnessClick = {
                        navController.navigate(Screen.Wellness.route)
                    },
                    onFreeTimeClick = {
                        navController.navigate(Screen.FreeTime.route)
                    },
                    onMotivationClick = {
                        navController.navigate(Screen.Motivation.route)
                    }
                )
            }
        }

        composable(Screen.RecommendationDetails.route) {
            MainScaffoldScreen(
                title = "Daily Recommendation",
                navController = navController,
                onLogoutClick = onLogoutClick
            ) {
                RecommendationDetailsScreen(userId = userId)
            }
        }

        composable(Screen.Nutrition.route) {
            MainScaffoldScreen(
                title = "Nutrition",
                navController = navController,
                onLogoutClick = onLogoutClick
            ) {
                NutritionScreen(userId = userId)
            }
        }

        composable(Screen.Wellness.route) {
            MainScaffoldScreen(
                title = "Wellness",
                navController = navController,
                onLogoutClick = onLogoutClick
            ) {
                WellnessScreen(userId = userId)
            }
        }

        composable(Screen.FreeTime.route) {
            MainScaffoldScreen(
                title = "Free Time",
                navController = navController,
                onLogoutClick = onLogoutClick
            ) {
                FreeTimeScreen(userId = userId)
            }
        }

        composable(Screen.Motivation.route) {
            MainScaffoldScreen(
                title = "Motivation",
                navController = navController,
                onLogoutClick = onLogoutClick
            ) {
                MotivationScreen(userId = userId)
            }
        }

        composable(Screen.Profile.route) {
            MainScaffoldScreen(
                title = "Profile",
                navController = navController,
                onLogoutClick = onLogoutClick
            ) {
                ProfileScreen(userId = userId)
            }
        }
    }
}