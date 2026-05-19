package com.example.activityapp.ui.screens

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
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
import com.example.activityapp.data.repository.ActivityRepository
import com.example.activityapp.ui.theme.*
import com.example.activityapp.ui.viewmodel.DashboardViewModel
import com.example.activityapp.data.remote.dto.user.UserResponse
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.layout.ContentScale
import androidx.compose.foundation.border
import androidx.compose.ui.draw.clip

@Composable
fun DashboardScreen(
    userId: Long,
    username: String = "User",
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

    var user by remember { mutableStateOf<UserResponse?>(null) }

    LaunchedEffect(userId) {
        dashboardViewModel.loadDashboardData(userId)

        try {
            activities = repository.getActivitiesByUser(userId)
        } catch (e: Exception) {
            weeklyStatsError = e.message ?: "Unknown error"
        }

        try {
            user = repository.getCurrentUser()
        } catch (e: Exception) {
        }
    }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = AppBackground
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(horizontal = 18.dp, vertical = 18.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            item {
                DashboardHeader(
                    username = user?.let { "${it.firstName}" } ?: "User"
                )
            }

            item {
                DailyActivityCard(
                    latestActivity = latestActivity,
                    errorMessage = errorMessage
                )
            }

            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    latestRecommendation?.let {
                        MainRecommendationCard(
                            modifier = Modifier.weight(1.35f),
                            recommendation = it,
                            onClick = onRecommendationClick
                        )
                    }

                    latestRecommendation?.motivation?.let {
                        MotivationCard(
                            modifier = Modifier.weight(0.85f),
                            motivation = it,
                            onClick = onMotivationClick
                        )
                    }
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
fun DashboardHeader(username: String) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(30.dp),
        colors = CardDefaults.cardColors(containerColor = WhiteSoft),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(18.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(68.dp)
                    .background(Cream, RoundedCornerShape(24.dp)),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(id = R.drawable.jakogamase),
                    contentDescription = null,
                    modifier = Modifier.size(44.dp)
                )
            }

            Spacer(modifier = Modifier.width(14.dp))

            Column {
                Text(
                    text = "Hello, $username",
                    style = MaterialTheme.typography.titleLarge,
                    color = TextPrimary,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = "Let’s make today active 🌿",
                    style = MaterialTheme.typography.bodyLarge,
                    color = TextSecondary
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
    val activityShape = RoundedCornerShape(
        topStart = 50.dp,
        topEnd = 24.dp,
        bottomStart = 32.dp,
        bottomEnd = 50.dp
    )

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .border(
                width = 2.dp,
                color = SavorySage.copy(alpha = 0.35f),
                shape = activityShape
            ),
        shape = activityShape,
        colors = CardDefaults.cardColors(containerColor = CardBackground),
        elevation = CardDefaults.cardElevation(defaultElevation = 10.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(250.dp)
        ) {

            Image(
                painter = painterResource(id = R.drawable.kartica1),
                contentDescription = null,
                modifier = Modifier
                    .fillMaxSize()
                    .alpha(0.22f),
                contentScale = ContentScale.Crop
            )

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(24.dp),
                verticalArrangement = Arrangement.SpaceBetween
            ) {

                Text(
                    text = "Today's steps",
                    style = MaterialTheme.typography.titleLarge,
                    color = TextPrimary,
                    fontWeight = FontWeight.Bold
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
                                verticalArrangement = Arrangement.spacedBy(10.dp)
                            ) {

                                Text(
                                    text = "$steps",
                                    style = MaterialTheme.typography.displayLarge,
                                    color = TextPrimary,
                                    fontWeight = FontWeight.ExtraBold
                                )

                                Text(
                                    text = "steps today",
                                    style = MaterialTheme.typography.titleMedium,
                                    color = TextSecondary
                                )

                                Box(
                                    modifier = Modifier
                                        .background(
                                            color = Cream.copy(alpha = 0.85f),
                                            shape = RoundedCornerShape(20.dp)
                                        )
                                        .padding(horizontal = 16.dp, vertical = 10.dp)
                                ) {
                                    Text(
                                        text = "Goal $goal",
                                        style = MaterialTheme.typography.bodyLarge,
                                        color = TextPrimary,
                                        fontWeight = FontWeight.SemiBold
                                    )
                                }
                            }

                            Box(
                                modifier = Modifier
                                    .background(
                                        color = Cream.copy(alpha = 0.55f),
                                        shape = CircleShape
                                    )
                                    .padding(14.dp)
                            ) {
                                CircularProgressSteps(progress = percentage)
                            }
                        }

                        Row(
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {

                            Box(
                                modifier = Modifier
                                    .background(
                                        color = AvocadoSmoothie.copy(alpha = 0.35f),
                                        shape = RoundedCornerShape(20.dp)
                                    )
                                    .padding(horizontal = 16.dp, vertical = 10.dp)
                            ) {
                                Text(
                                    text = "$activityLevel activity",
                                    style = MaterialTheme.typography.bodyLarge,
                                    color = TextPrimary,
                                    fontWeight = FontWeight.SemiBold
                                )
                            }

                            Box(
                                modifier = Modifier
                                    .background(
                                        color = PeachProtein.copy(alpha = 0.55f),
                                        shape = RoundedCornerShape(20.dp)
                                    )
                                    .padding(horizontal = 16.dp, vertical = 10.dp)
                            ) {
                                Text(
                                    text = "${(percentage * 100).toInt()}% completed",
                                    style = MaterialTheme.typography.bodyLarge,
                                    color = TextPrimary,
                                    fontWeight = FontWeight.SemiBold
                                )
                            }
                        }
                    }

                    !errorMessage.isNullOrEmpty() -> {

                        Text(
                            text = "Error: $errorMessage",
                            style = MaterialTheme.typography.bodyLarge,
                            color = BlushBeet
                        )
                    }

                    else -> {

                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {

                            CircularProgressIndicator(
                                modifier = Modifier.size(28.dp),
                                color = SavorySage,
                                strokeWidth = 4.dp
                            )

                            Text(
                                text = "Loading activity...",
                                style = MaterialTheme.typography.bodyLarge,
                                color = TextSecondary
                            )
                        }
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
        modifier = Modifier.size(118.dp)
    ) {
        Canvas(modifier = Modifier.size(104.dp)) {
            drawArc(
                color = Cream,
                startAngle = -90f,
                sweepAngle = 360f,
                useCenter = false,
                style = Stroke(width = 22f, cap = StrokeCap.Round)
            )

            drawArc(
                color = SavorySage,
                startAngle = -90f,
                sweepAngle = 360f * progress,
                useCenter = false,
                style = Stroke(width = 22f, cap = StrokeCap.Round)
            )
        }

        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = "${(progress * 100).toInt()}%",
                style = MaterialTheme.typography.titleLarge,
                color = TextPrimary,
                fontWeight = FontWeight.Bold
            )

            Text(
                text = "done",
                style = MaterialTheme.typography.bodyMedium,
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
        else -> AvocadoSmoothie.copy(alpha = 0.40f)
    }

    Box(
        modifier = Modifier
            .background(backgroundColor, RoundedCornerShape(20.dp))
            .padding(horizontal = 18.dp, vertical = 10.dp)
    ) {
        Text(
            text = "Activity level: $level",
            style = MaterialTheme.typography.bodyLarge,
            color = TextPrimary,
            fontWeight = FontWeight.SemiBold
        )
    }
}

fun formatDailyState(state: String?): String {
    return when (state) {
        "VERY_LOW_ACTIVITY" -> "Very low"
        "LOW_ACTIVITY" -> "Low"
        "ON_TRACK" -> "On track"
        "NEAR_GOAL" -> "Near goal"
        "GOAL_ACHIEVED" -> "Goal achieved"
        "RECOVERY_DAY" -> "Recovery"
        "ABOVE_USUAL_ACTIVITY" -> "Above usual"
        null -> "No data"
        else -> state
    }
}

@Composable
fun MainRecommendationCard(
    modifier: Modifier = Modifier,
    recommendation: RecommendationResponse,
    onClick: () -> Unit
) {
    Card(
        modifier = modifier
            .height(210.dp)
            .clickable { onClick() }
            .border(
                width = 2.dp,
                color = PeachProtein.copy(alpha = 0.55f),
                shape = RoundedCornerShape(
                    topStart = 42.dp,
                    topEnd = 18.dp,
                    bottomStart = 18.dp,
                    bottomEnd = 42.dp
                )
            ),
        shape = RoundedCornerShape(
            topStart = 42.dp,
            topEnd = 18.dp,
            bottomStart = 18.dp,
            bottomEnd = 42.dp
        ),
        colors = CardDefaults.cardColors(
            containerColor = WhiteSoft
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 6.dp
        )
    ){
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {

            Column(
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {

                Box(
                    modifier = Modifier
                        .size(38.dp)
                        .background(
                            PeachProtein.copy(alpha = 0.45f),
                            CircleShape
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "✿",
                        style = MaterialTheme.typography.titleLarge,
                        color = TextPrimary,
                        fontWeight = FontWeight.Bold
                    )
                }

                Text(
                    text = "Daily recommendation",
                    style = MaterialTheme.typography.titleMedium,
                    color = TextPrimary,
                    fontWeight = FontWeight.Bold
                )

                Text(
                    text = recommendation.message ?: "No message",
                    style = MaterialTheme.typography.bodyMedium,
                    color = TextPrimary,
                    maxLines = 3
                )
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {

                InfoChip(
                    modifier = Modifier.weight(1f),
                    title = "Intensity",
                    value = recommendation.intensity ?: "No data",
                    background = PeachProtein
                )

                InfoChip(
                    modifier = Modifier.weight(1f),
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
    modifier: Modifier = Modifier,
    motivation: MotivationResponse,
    onClick: () -> Unit
) {
    Card(
        modifier = modifier
            .height(210.dp)
            .clickable { onClick() }
            .border(
                width = 2.dp,
                color = AvocadoSmoothie.copy(alpha = 0.45f),
                shape = RoundedCornerShape(
                    topStart = 18.dp,
                    topEnd = 42.dp,
                    bottomStart = 42.dp,
                    bottomEnd = 18.dp
                )
            ),
        shape = RoundedCornerShape(
            topStart = 18.dp,
            topEnd = 42.dp,
            bottomStart = 42.dp,
            bottomEnd = 18.dp
        ),
        colors = CardDefaults.cardColors(
            containerColor = Cream
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 6.dp
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(38.dp)
                    .background(AvocadoSmoothie.copy(alpha = 0.45f), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "✦",
                    style = MaterialTheme.typography.titleLarge,
                    color = TextPrimary,
                    fontWeight = FontWeight.Bold
                )
            }

            Text(
                text = "Motivation",
                style = MaterialTheme.typography.titleMedium,
                color = TextPrimary,
                fontWeight = FontWeight.Bold
            )

            Text(
                text = motivation.message ?: "No data",
                style = MaterialTheme.typography.bodyMedium,
                color = TextPrimary,
                maxLines = 4
            )
        }
    }
}

@Composable
fun InfoChip(
    modifier: Modifier = Modifier,
    title: String,
    value: String,
    background: androidx.compose.ui.graphics.Color
) {
    Column(
        modifier = modifier
            .background(
                color = background.copy(alpha = 0.70f),
                shape = RoundedCornerShape(18.dp)
            )
            .padding(horizontal = 6.dp, vertical = 8.dp)
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.bodySmall,
            color = TextSecondary
        )

        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium,
            color = TextPrimary,
            fontWeight = FontWeight.Bold
        )
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

    val weeklyShape = RoundedCornerShape(
        topStart = 26.dp,
        topEnd = 46.dp,
        bottomStart = 46.dp,
        bottomEnd = 26.dp
    )

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .border(
                width = 1.8.dp,
                color = OatLatte.copy(alpha = 0.7f),
                shape = weeklyShape
            ),
        shape = weeklyShape,
        colors = CardDefaults.cardColors(containerColor = WhiteSoft),
        elevation = CardDefaults.cardElevation(defaultElevation = 7.dp)
    ) {
        Column(
            modifier = Modifier.padding(22.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            Text(
                text = "Weekly statistics",
                style = MaterialTheme.typography.titleLarge,
                color = TextPrimary,
                fontWeight = FontWeight.Bold
            )

            when {
                errorMessage.isNotEmpty() -> {
                    Text(
                        text = "Error: $errorMessage",
                        style = MaterialTheme.typography.bodyLarge,
                        color = BlushBeet
                    )
                }

                activities.isEmpty() -> {
                    Text(
                        text = "No activity data available.",
                        style = MaterialTheme.typography.bodyLarge,
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
    val miniShape = RoundedCornerShape(
        topStart = 26.dp,
        topEnd = 14.dp,
        bottomStart = 14.dp,
        bottomEnd = 26.dp
    )

    Card(
        modifier = modifier.border(
            width = 1.dp,
            color = WhiteSoft.copy(alpha = 0.7f),
            shape = miniShape
        ),
        shape = miniShape,
        colors = CardDefaults.cardColors(containerColor = background),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
    ) {
        Column(
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 14.dp),
            horizontalAlignment = Alignment.Start
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.bodyMedium,
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
                style = MaterialTheme.typography.bodyMedium,
                color = TextSecondary
            )
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
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Text(
            text = "Steps by day",
            style = MaterialTheme.typography.titleMedium,
            color = TextPrimary,
            fontWeight = FontWeight.Bold
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

@Composable
fun LegendItem(
    color: androidx.compose.ui.graphics.Color,
    text: String
) {
    Row(verticalAlignment = Alignment.CenterVertically) {
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