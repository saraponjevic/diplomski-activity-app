package com.example.activityapp.ui.screens


import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.activityapp.R
import com.example.activityapp.data.remote.dto.ActivityResponse
import com.example.activityapp.data.remote.dto.MotivationResponse
import com.example.activityapp.data.remote.dto.RecommendationResponse

import com.example.activityapp.ui.theme.AppBackground
import com.example.activityapp.ui.theme.AvocadoSmoothie
import com.example.activityapp.ui.theme.BlushBeet
import com.example.activityapp.ui.theme.CardBackground
import com.example.activityapp.ui.theme.Cream
import com.example.activityapp.ui.theme.OatLatte
import com.example.activityapp.ui.theme.PeachProtein
import com.example.activityapp.ui.theme.SavorySage
import com.example.activityapp.ui.theme.TextPrimary
import com.example.activityapp.ui.theme.TextSecondary
import com.example.activityapp.ui.theme.WhiteSoft
import com.example.activityapp.ui.viewmodel.DashboardViewModel
import com.example.activityapp.data.repository.ActivityRepository

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

    val context = LocalContext.current
    val repository = remember { ActivityRepository(context) }

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

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = AppBackground
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(horizontal = 20.dp, vertical = 18.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    Box(
                        modifier = Modifier
                            .size(64.dp)
                            .background(Cream, shape = RoundedCornerShape(20.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.jagodaa),
                            contentDescription = null,
                            modifier = Modifier.size(40.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(12.dp))

                    Box(
                        modifier = Modifier
                            .background(
                                color = CardBackground,
                                shape = RoundedCornerShape(20.dp)
                            )
                            .padding(horizontal = 16.dp, vertical = 12.dp)
                    ) {
                        Text(
                            text = "Hello, ${latestActivity?.let { "User" } ?: ""}",
                            style = MaterialTheme.typography.bodyMedium,
                            color = TextPrimary
                        )
                    }
                    Box(
                        modifier = Modifier
                            .size(8.dp)
                            .background(CardBackground, shape = CircleShape)
                            .offset(x = (-6).dp, y = 10.dp)
                    )
                }
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
}

@Composable
fun DailyActivityCard(
    latestActivity: ActivityResponse?,
    errorMessage: String?
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(28.dp),
        colors = CardDefaults.cardColors(containerColor = CardBackground),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Column(
            modifier = Modifier.padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(18.dp)
        ) {
            Text(
                text = "Today’s steps",
                style = MaterialTheme.typography.titleLarge,
                color = TextPrimary,
                fontWeight = FontWeight.SemiBold
            )

            when {
                latestActivity != null -> {
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

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(
                            modifier = Modifier.weight(1f)
                        ) {
                            Text(
                                text = "Steps today",
                                style = MaterialTheme.typography.bodyMedium,
                                color = TextSecondary
                            )

                            Spacer(modifier = Modifier.height(6.dp))

                            Text(
                                text = "$steps",
                                style = MaterialTheme.typography.headlineLarge,
                                color = TextPrimary,
                                fontWeight = FontWeight.Bold
                            )

                            Spacer(modifier = Modifier.height(6.dp))

                            Text(
                                text = "Goal: $goal",
                                style = MaterialTheme.typography.bodyMedium,
                                color = TextSecondary
                            )
                        }

                        CircularProgressSteps(progress = percentage)
                    }

                    ActivityLevelBadge(activityLevel)
                }

                !errorMessage.isNullOrEmpty() -> {
                    Text(
                        text = "Error: $errorMessage",
                        style = MaterialTheme.typography.bodyMedium,
                        color = BlushBeet
                    )
                }

                else -> {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(22.dp),
                            color = SavorySage,
                            strokeWidth = 2.5.dp
                        )

                        Text(
                            text = "Loading activity...",
                            style = MaterialTheme.typography.bodyMedium,
                            color = TextSecondary
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun CircularProgressSteps(progress: Float) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier.size(96.dp)
    ) {
        Canvas(modifier = Modifier.size(96.dp)) {
            drawArc(
                color = Cream,
                startAngle = -90f,
                sweepAngle = 360f,
                useCenter = false,
                style = Stroke(width = 16f, cap = StrokeCap.Round)
            )

            drawArc(
                color = SavorySage,
                startAngle = -90f,
                sweepAngle = 360f * progress,
                useCenter = false,
                style = Stroke(width = 16f, cap = StrokeCap.Round)
            )
        }

        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = "${(progress * 100).toInt()}%",
                style = MaterialTheme.typography.titleMedium,
                color = TextPrimary,
                fontWeight = FontWeight.Bold
            )

            Text(
                text = "done",
                style = MaterialTheme.typography.bodySmall,
                color = TextSecondary
            )
        }
    }
}

@Composable
fun ActivityLevelBadge(level: String) {
    val backgroundColor = when (level) {
        "Low" -> BlushBeet.copy(alpha = 0.30f)
        "Moderate" -> PeachProtein.copy(alpha = 0.65f)
        else -> AvocadoSmoothie.copy(alpha = 0.35f)
    }

    val textColor = when (level) {
        "Low" -> TextPrimary
        "Moderate" -> TextPrimary
        else -> TextPrimary
    }

    Box(
        modifier = Modifier
            .background(backgroundColor, RoundedCornerShape(18.dp))
            .padding(horizontal = 16.dp, vertical = 9.dp)
    ) {
        Text(
            text = "Activity level: $level",
            style = MaterialTheme.typography.labelLarge,
            color = textColor,
            fontWeight = FontWeight.SemiBold
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
            .clickable { onClick() },
        shape = RoundedCornerShape(28.dp),
        colors = CardDefaults.cardColors(containerColor = WhiteSoft),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Column(
            modifier = Modifier.padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Text(
                text = "Daily recommendation",
                style = MaterialTheme.typography.titleLarge,
                color = TextPrimary,
                fontWeight = FontWeight.SemiBold
            )

            Box(
                modifier = Modifier
                    .background(
                        color = AvocadoSmoothie.copy(alpha = 0.28f),
                        shape = RoundedCornerShape(16.dp)
                    )
                    .padding(horizontal = 14.dp, vertical = 8.dp)
            ) {
                Text(
                    text = formatDailyState(recommendation.dailyState),
                    style = MaterialTheme.typography.bodyMedium,
                    color = TextPrimary,
                    fontWeight = FontWeight.Medium
                )
            }

            Text(
                text = recommendation.message ?: "No message",
                style = MaterialTheme.typography.bodyLarge,
                color = TextPrimary
            )

            Row(
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                InfoChip(
                    title = "Intensity",
                    value = recommendation.intensity ?: "No data",
                    background = PeachProtein
                )

                InfoChip(
                    title = "Duration",
                    value = "${recommendation.durationMinutes ?: 0} min",
                    background = OatLatte
                )
            }
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
            .clickable { onClick() },
        shape = RoundedCornerShape(28.dp),
        colors = CardDefaults.cardColors(containerColor = CardBackground),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Column(
            modifier = Modifier.padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Text(
                text = "Motivation",
                style = MaterialTheme.typography.titleLarge,
                color = TextPrimary,
                fontWeight = FontWeight.SemiBold
            )

            Text(
                text = motivation.message ?: "No data",
                style = MaterialTheme.typography.bodyLarge,
                color = TextPrimary
            )
        }
    }
}

@Composable
fun InfoChip(
    title: String,
    value: String,
    background: androidx.compose.ui.graphics.Color
) {
    Column(
        modifier = Modifier
            .background(
                color = background.copy(alpha = 0.65f),
                shape = RoundedCornerShape(18.dp)
            )
            .padding(horizontal = 14.dp, vertical = 10.dp)
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.bodySmall,
            color = TextSecondary
        )

        Spacer(modifier = Modifier.height(2.dp))

        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium,
            color = TextPrimary,
            fontWeight = FontWeight.SemiBold
        )
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
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Text(
            text = "Steps by day",
            style = MaterialTheme.typography.titleMedium,
            color = TextPrimary,
            fontWeight = FontWeight.SemiBold
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(190.dp),
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
                        style = MaterialTheme.typography.bodySmall,
                        color = TextSecondary
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    WeeklyBar(
                        fraction = fraction,
                        isGoalReached = activity.steps >= activity.goalSteps
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = formatShortDate(activity.date),
                        style = MaterialTheme.typography.bodySmall,
                        color = TextSecondary,
                        textAlign = TextAlign.Center
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(2.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            LegendItem(
                color = SavorySage,
                text = "Goal not reached"
            )

            LegendItem(
                color = AvocadoSmoothie,
                text = "Goal reached"
            )
        }
    }
}

@Composable
fun LegendItem(
    color: androidx.compose.ui.graphics.Color,
    text: String
) {
    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(12.dp)
                .background(color = color, shape = RoundedCornerShape(4.dp))
        )

        Spacer(modifier = Modifier.width(6.dp))

        Text(
            text = text,
            style = MaterialTheme.typography.bodySmall,
            color = TextSecondary
        )
    }
}

@Composable
fun WeeklyBar(
    fraction: Float,
    isGoalReached: Boolean,
    modifier: Modifier = Modifier
) {
    val barColor = if (isGoalReached) AvocadoSmoothie else SavorySage

    Canvas(
        modifier = modifier
            .width(30.dp)
            .height(112.dp)
    ) {
        val barWidth = size.width
        val barHeight = size.height * fraction.coerceIn(0f, 1f)

        drawRoundRect(
            color = Cream,
            topLeft = Offset(0f, 0f),
            size = Size(barWidth, size.height),
            cornerRadius = CornerRadius(14f, 14f)
        )

        drawRoundRect(
            color = barColor,
            topLeft = Offset(0f, size.height - barHeight),
            size = Size(barWidth, barHeight),
            cornerRadius = CornerRadius(14f, 14f)
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
            .clickable { onClick() },
        shape = RoundedCornerShape(28.dp),
        colors = CardDefaults.cardColors(containerColor = WhiteSoft),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Column(
            modifier = Modifier.padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = "Weekly statistics",
                style = MaterialTheme.typography.titleLarge,
                color = TextPrimary,
                fontWeight = FontWeight.SemiBold
            )

            when {
                errorMessage.isNotEmpty() -> {
                    Text(
                        text = "Error: $errorMessage",
                        style = MaterialTheme.typography.bodyMedium,
                        color = BlushBeet
                    )
                }

                activities.isEmpty() -> {
                    Text(
                        text = "No activity data available.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = TextSecondary
                    )
                }

                else -> {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        StatsMiniCard(
                            modifier = Modifier.weight(1f),
                            title = "Average",
                            value = averageSteps.toInt().toString(),
                            subtitle = "steps",
                            background = PeachProtein
                        )

                        StatsMiniCard(
                            modifier = Modifier.weight(1f),
                            title = "Reached",
                            value = goalReachedDays.toString(),
                            subtitle = "days",
                            background = OatLatte
                        )

                        StatsMiniCard(
                            modifier = Modifier.weight(1f),
                            title = "Tracked",
                            value = activities.size.toString(),
                            subtitle = "days",
                            background = AvocadoSmoothie.copy(alpha = 0.55f)
                        )
                    }

                    Spacer(modifier = Modifier.height(4.dp))

                    WeeklyStepsBarChart(activities = activities)
                }
            }
        }
    }
}

@Composable
fun StatsMiniCard(
    modifier: Modifier = Modifier,
    title: String,
    value: String,
    subtitle: String,
    background: androidx.compose.ui.graphics.Color
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = background),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Column(
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 14.dp),
            horizontalAlignment = Alignment.Start
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.bodySmall,
                color = TextSecondary
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = value,
                style = MaterialTheme.typography.titleLarge,
                color = TextPrimary,
                fontWeight = FontWeight.Bold
            )

            Text(
                text = subtitle,
                style = MaterialTheme.typography.bodySmall,
                color = TextSecondary
            )
        }
    }
}