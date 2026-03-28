package com.example.activityapp.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.activityapp.data.remote.dto.*
import com.example.activityapp.ui.viewmodel.DashboardViewModel

@Composable
fun DashboardScreen(
    userId: Long,
    onRecommendationClick: () -> Unit,
    onNutritionClick: () -> Unit,
    onWellnessClick: () -> Unit,
    onFreeTimeClick: () -> Unit,
    onMotivationClick: () -> Unit,
    dashboardViewModel: DashboardViewModel = viewModel()
) {
    val latestActivity by dashboardViewModel.latestActivity.collectAsState()
    val latestRecommendation by dashboardViewModel.latestRecommendation.collectAsState()
    val errorMessage by dashboardViewModel.errorMessage.collectAsState()

    LaunchedEffect(userId) {
        dashboardViewModel.loadDashboardData(userId)
    }

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Text(
                text = "Daily Activity",
                style = MaterialTheme.typography.headlineMedium
            )
        }

        item {
            Card(modifier = Modifier.fillMaxWidth()) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    if (latestActivity != null) {
                        val steps = latestActivity!!.steps
                        val goal = latestActivity!!.goalSteps

                        val percentage = if (goal > 0) {
                            (steps.toDouble() / goal.toDouble()) * 100
                        } else 0.0

                        val activityLevel = when {
                            steps < 3000 -> "Low"
                            steps < 7000 -> "Moderate"
                            else -> "High"
                        }

                        Text("Steps today: $steps")
                        Text("Daily goal: $goal")
                        Text("Goal reached: ${"%.1f".format(percentage)}%")
                        Text("Activity level: $activityLevel")
                    } else {
                        Text("Loading activity...")
                    }

                    if (errorMessage != null) {
                        Text("Error: $errorMessage")
                    }
                }
            }
        }

        item {
            latestRecommendation?.let {
                MainRecommendationCard(it, onRecommendationClick)
            }
        }

        item {
            latestRecommendation?.nutrition?.let {
                NutritionCard(it, onNutritionClick)
            }
        }

        item {
            latestRecommendation?.wellness?.let {
                WellnessCard(it, onWellnessClick)
            }
        }

        item {
            latestRecommendation?.freeTime?.let {
                FreeTimeCard(it, onFreeTimeClick)
            }
        }

        item {
            latestRecommendation?.motivation?.let {
                MotivationCard(it, onMotivationClick)
            }
        }
    }
}

fun formatDailyState(state: String): String {
    return when (state) {
        "VERY_LOW_ACTIVITY" -> "Very low activity"
        "LOW_ACTIVITY" -> "Low activity"
        "ON_TRACK" -> "On track"
        "NEAR_GOAL" -> "Near goal"
        "GOAL_ACHIEVED" -> "Goal achieved"
        "RECOVERY_DAY" -> "Recovery day"
        "ABOVE_USUAL_ACTIVITY" -> "Above usual activity"
        else -> state
    }
}

@Composable
fun MainRecommendationCard(
    recommendation: RecommendationResponse,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text("Daily Recommendation", fontWeight = FontWeight.Bold)
            Text("Daily state: ${formatDailyState(recommendation.dailyState)}")
            Text(recommendation.message)
            Text("Type: ${recommendation.recommendationType}")
            Text("Intensity: ${recommendation.intensity}")
            Text("Duration: ${recommendation.durationMinutes} min")
        }
    }
}

@Composable
fun NutritionCard(nutrition: NutritionResponse, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
    ) {
        Column(Modifier.padding(16.dp)) {
            Text("Nutrition", fontWeight = FontWeight.Bold)
            Text(nutrition.mealSuggestion ?: "No data")
        }
    }
}

@Composable
fun WellnessCard(wellness: WellnessResponse, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
    ) {
        Column(Modifier.padding(16.dp)) {
            Text("Wellness", fontWeight = FontWeight.Bold)
            Text(wellness.wellnessTip ?: "No data")
        }
    }
}

@Composable
fun FreeTimeCard(freeTime: FreeTimeResponse, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
    ) {
        Column(Modifier.padding(16.dp)) {
            Text("Free Time", fontWeight = FontWeight.Bold)
            Text(freeTime.activitySuggestion ?: "No data")
        }
    }
}

@Composable
fun MotivationCard(motivation: MotivationResponse, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
    ) {
        Column(Modifier.padding(16.dp)) {
            Text("Motivation", fontWeight = FontWeight.Bold)
            Text(motivation.message ?: "No data")
        }
    }
}