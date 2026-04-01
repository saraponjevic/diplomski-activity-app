package com.example.activityapp.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.activityapp.ui.screens.ChangePasswordScreen
import com.example.activityapp.ui.screens.DashboardScreen
import com.example.activityapp.ui.screens.EditProfileScreen
import com.example.activityapp.ui.screens.MotivationScreen
import com.example.activityapp.ui.screens.ProfileScreen
import com.example.activityapp.ui.screens.RecommendationDetailsScreen
import com.example.activityapp.ui.screens.WellnessScreen
import com.example.activityapp.ui.screens.freetime.FreeTimeScreen
import com.example.activityapp.ui.screens.nutrition.NutritionScreen
import com.example.activityapp.ui.screens.planner.PlannerScreen

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
                    onMotivationClick = {
                        navController.navigate(Screen.Motivation.route)
                    },
                    onWeeklyStatsClick = {
                        navController.navigate(Screen.WeeklyStats.route)
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
                WellnessScreen(
                    userId = userId,
                    onBackToPrevious = {
                        navController.popBackStack()
                    }
                )
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
                ProfileScreen(
                    userId = userId,
                    onEditProfileClick = {
                        navController.navigate("edit_profile")
                    },
                    onChangePasswordClick = {
                        navController.navigate("change_password")
                    }
                )
            }
        }

        composable("edit_profile") {
            MainScaffoldScreen(
                title = "Edit Profile",
                navController = navController,
                onLogoutClick = onLogoutClick
            ) {
                EditProfileScreen(
                    userId = userId,
                    onBackClick = { navController.popBackStack() },
                    onSaveSuccess = { navController.popBackStack() }
                )
            }
        }

        composable("change_password") {
            MainScaffoldScreen(
                title = "Change Password",
                navController = navController,
                onLogoutClick = onLogoutClick
            ) {
                ChangePasswordScreen(
                    userId = userId,
                    onBackClick = { navController.popBackStack() },
                    onSaveSuccess = { navController.popBackStack() }
                )
            }
        }

        composable(Screen.Planner.route) {
            MainScaffoldScreen(
                title = "Planner",
                navController = navController,
                onLogoutClick = onLogoutClick
            ) {
                PlannerScreen(userId = userId)
            }
        }
    }
}