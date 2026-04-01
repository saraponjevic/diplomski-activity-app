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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.activityapp.data.remote.dto.ActivityResponse
import com.example.activityapp.data.remote.dto.MotivationResponse
import com.example.activityapp.data.remote.dto.RecommendationResponse
import com.example.activityapp.data.repository.ActivityRepository
import com.example.activityapp.ui.viewmodel.DashboardViewModel
import java.util.Locale

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.Alignment
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign

@Composable
fun DashboardScreen(
    userId: Long,
    onRecommendationClick: () -> Unit,
    onMotivationClick: () -> Unit,
    onWeeklyStatsClick: () -> Unit = {},
    dashboardViewModel: DashboardViewModel = viewModel()
) {
    val latestActivity by dashboardViewModel.latestActivity.collectAsState()
    val latestRecommendation by dashboardViewModel.latestRecommendation.collectAsState()
    val errorMessage by dashboardViewModel.errorMessage.collectAsState()

    val repository = remember { ActivityRepository() }

    var activities by remember { mutableStateOf<List<ActivityResponse>>(emptyList()) }
    var weeklyStatsError by remember { mutableStateOf("") }

    LaunchedEffect(userId) {
        dashboardViewModel.loadDashboardData(userId)

        try {
            activities = repository.getActivitiesByUser(userId)
        } catch (e: Exception) {
            weeklyStatsError = e.message ?: "Unknown error"
        }
    }

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Text(
                text = "Daily Activity",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold
            )
        }

        item {
            DailyActivityCard(
                latestActivity = latestActivity,
                errorMessage = errorMessage
            )
        }

        item {
            latestRecommendation?.let {
                MainRecommendationCard(
                    recommendation = it,
                    onClick = onRecommendationClick
                )
            }
        }

        item {
            latestRecommendation?.motivation?.let {
                MotivationCard(
                    motivation = it,
                    onClick = onMotivationClick
                )
            }
        }

        item {
            WeeklyStatsCard(
                activities = activities,
                errorMessage = weeklyStatsError,
                onClick = onWeeklyStatsClick
            )
        }
    }
}

@Composable
fun DailyActivityCard(
    latestActivity: ActivityResponse?,
    errorMessage: String?
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp)
    ) {
        Column(
            modifier = Modifier.padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            if (latestActivity != null) {
                val steps = latestActivity.steps
                val goal = latestActivity.goalSteps

                val percentage = if (goal > 0) {
                    (steps.toFloat() / goal.toFloat()).coerceIn(0f, 1f)
                } else 0f

                val activityLevel = when {
                    steps < 3000 -> "Low"
                    steps < 7000 -> "Moderate"
                    else -> "High"
                }

                // 🔵 GORNJI DEO (steps + krug)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    Column {
                        Text(
                            "Steps today",
                            style = MaterialTheme.typography.bodyMedium
                        )

                        Text(
                            "$steps",
                            style = MaterialTheme.typography.headlineLarge,
                            fontWeight = FontWeight.Bold
                        )

                        Text(
                            "Goal: $goal",
                            style = MaterialTheme.typography.bodySmall
                        )
                    }

                    CircularProgressSteps(percentage)
                }

                ActivityLevelBadge(activityLevel)
            } else {
                Text("Loading activity...")
            }

            if (!errorMessage.isNullOrEmpty()) {
                Text("Error: $errorMessage")
            }
        }
    }
}

@Composable
fun CircularProgressSteps(progress: Float) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier.size(90.dp)
    ) {
        Canvas(modifier = Modifier.size(90.dp)) {

            drawArc(
                color = Color(0xFFE0E0E0),
                startAngle = 0f,
                sweepAngle = 360f,
                useCenter = false,
                style = androidx.compose.ui.graphics.drawscope.Stroke(width = 14f)
            )

            drawArc(
                color = Color(0xFF7E57C2),
                startAngle = -90f,
                sweepAngle = 360f * progress,
                useCenter = false,
                style = androidx.compose.ui.graphics.drawscope.Stroke(width = 14f)
            )
        }

        Text(
            text = "${(progress * 100).toInt()}%",
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
fun ActivityLevelBadge(level: String) {

    val color = when (level) {
        "Low" -> Color(0xFFE57373)
        "Moderate" -> Color(0xFFFFB74D)
        else -> Color(0xFF66BB6A)
    }

    Box(
        modifier = Modifier
            .background(color.copy(alpha = 0.15f), RoundedCornerShape(16.dp))
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        Text(
            text = "Activity level: $level",
            color = color,
            fontWeight = FontWeight.Bold
        )
    }
}



fun formatDailyState(state: String?): String {
    return when (state) {
        "VERY_LOW_ACTIVITY" -> "Very low activity"
        "LOW_ACTIVITY" -> "Low activity"
        "ON_TRACK" -> "On track"
        "NEAR_GOAL" -> "Near goal"
        "GOAL_ACHIEVED" -> "Goal achieved"
        "RECOVERY_DAY" -> "Recovery day"
        "ABOVE_USUAL_ACTIVITY" -> "Above usual activity"
        null -> "No data"
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
            Text( formatDailyState(recommendation.dailyState))
            Text(recommendation.message ?: "No message")
            Text("Intensity: ${recommendation.intensity ?: "No data"}")
            Text("Duration: ${recommendation.durationMinutes ?: 0} min")
        }
    }
}

@Composable
fun MotivationCard(
    motivation: MotivationResponse,
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
            Text("Motivation", fontWeight = FontWeight.Bold)
            Text(motivation.message ?: "No data")
        }
    }
}


@Composable
fun WeeklyStepsBarChart(
    activities: List<ActivityResponse>,
    modifier: Modifier = Modifier
) {
    val chartData = activities.takeLast(7)
    val maxSteps = (chartData.maxOfOrNull { it.steps } ?: 1).coerceAtLeast(1)

    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            text = "Steps by day",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.SemiBold
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(180.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Bottom
        ) {
            chartData.forEach { activity ->
                val fraction = activity.steps.toFloat() / maxSteps.toFloat()

                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Bottom
                ) {
                    Text(
                        text = activity.steps.toString(),
                        style = MaterialTheme.typography.bodySmall
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    WeeklyBar(
                        fraction = fraction,
                        isGoalReached = activity.steps >= activity.goalSteps
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    Text(
                        text = formatShortDate(activity.date),
                        style = MaterialTheme.typography.bodySmall,
                        textAlign = TextAlign.Center
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(4.dp))

        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(12.dp)
                    .background(
                        color = Color(0xFF7E57C2),
                        shape = RoundedCornerShape(4.dp)
                    )
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text("Goal not reached", style = MaterialTheme.typography.bodySmall)

            Spacer(modifier = Modifier.width(16.dp))

            Box(
                modifier = Modifier
                    .size(12.dp)
                    .background(
                        color = Color(0xFF66BB6A),
                        shape = RoundedCornerShape(4.dp)
                    )
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text("Goal reached", style = MaterialTheme.typography.bodySmall)
        }
    }
}

@Composable
fun WeeklyBar(
    fraction: Float,
    isGoalReached: Boolean,
    modifier: Modifier = Modifier
) {
    val barColor = if (isGoalReached) Color(0xFF66BB6A) else Color(0xFF7E57C2)

    Canvas(
        modifier = modifier
            .width(28.dp)
            .height(110.dp)
    ) {
        val barWidth = size.width
        val barHeight = size.height * fraction.coerceIn(0f, 1f)

        drawRoundRect(
            color = Color(0xFFE6E0EC),
            topLeft = Offset(0f, 0f),
            size = Size(barWidth, size.height),
            cornerRadius = CornerRadius(12f, 12f)
        )

        drawRoundRect(
            color = barColor,
            topLeft = Offset(0f, size.height - barHeight),
            size = Size(barWidth, barHeight),
            cornerRadius = CornerRadius(12f, 12f)
        )
    }
}

fun formatShortDate(date: String?): String {
    if (date.isNullOrBlank()) return ""

    return if (date.length >= 10) {
        val parts = date.substring(0, 10).split("-")
        if (parts.size == 3) {
            "${parts[2]}.${parts[1]}."
        } else {
            date.take(5)
        }
    } else {
        date.take(5)
    }
}

@Composable
fun WeeklyStatsCard(
    activities: List<ActivityResponse>,
    errorMessage: String,
    onClick: () -> Unit
) {
    val averageSteps = if (activities.isNotEmpty()) {
        activities.map { it.steps }.average()
    } else {
        0.0
    }

    val goalReachedDays = activities.count { it.steps >= it.goalSteps }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text("Weekly Statistics", fontWeight = FontWeight.Bold)

            when {
                errorMessage.isNotEmpty() -> {
                    Text("Error: $errorMessage")
                }

                activities.isEmpty() -> {
                    Text("No activity data available.")
                }

                else -> {
                    Text("Average steps: ${averageSteps.toInt()}")
                    Text("Goal reached days: $goalReachedDays")
                    Text("Tracked days: ${activities.size}")

                    Spacer(modifier = Modifier.height(12.dp))

                    WeeklyStepsBarChart(activities = activities)
                }
            }
        }
    }


}