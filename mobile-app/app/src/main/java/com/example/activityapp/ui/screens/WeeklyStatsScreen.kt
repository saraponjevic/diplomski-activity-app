package com.example.activityapp.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.activityapp.data.remote.dto.ActivityResponse
import com.example.activityapp.data.repository.ActivityRepository

@Composable
fun WeeklyStatsScreen(
    userId: Long,
    onBackClick: () -> Unit
) {
    val repository = ActivityRepository()

    var activities by remember { mutableStateOf<List<ActivityResponse>>(emptyList()) }
    var errorMessage by remember { mutableStateOf("") }

    LaunchedEffect(userId) {
        try {
            activities = repository.getActivitiesByUser(userId)
        } catch (e: Exception) {
            errorMessage = e.message ?: "Unknown error"
        }
    }

    val averageSteps = if (activities.isNotEmpty()) {
        activities.map { it.steps }.average()
    } else 0.0

    val goalReachedDays = activities.count { it.steps >= it.goalSteps }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = "Weekly Statistics",
            style = MaterialTheme.typography.headlineMedium
        )

        when {
            errorMessage.isNotEmpty() -> {
                Text(text = "Error: $errorMessage")
            }

            activities.isEmpty() -> {
                Text(text = "No activity data available.")
            }

            else -> {
                Card(modifier = Modifier.fillMaxWidth()) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text("Daily steps:")
                        activities.forEach {
                            Text("- ${it.date}: ${it.steps} steps")
                        }

                        Text("Average steps: $averageSteps")
                        Text("Goal reached days: $goalReachedDays")
                    }
                }
            }
        }

        Button(
            onClick = onBackClick,
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
        ) {
            Text("Back")
        }
    }
}