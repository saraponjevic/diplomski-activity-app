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
import com.example.activityapp.data.remote.dto.RecommendationResponse
import com.example.activityapp.data.repository.ActivityRepository

@Composable
fun RecommendationsScreen(
    userId: Long,
    onBackClick: () -> Unit
) {
    val repository = ActivityRepository()

    var recommendation by remember { mutableStateOf<RecommendationResponse?>(null) }
    var errorMessage by remember { mutableStateOf("") }

    LaunchedEffect(userId) {
        try {
            recommendation = repository.getLatestRecommendation(userId)
        } catch (e: Exception) {
            errorMessage = e.message ?: "Unknown error"
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = "Recommendations",
            style = MaterialTheme.typography.headlineMedium
        )

        when {
            errorMessage.isNotEmpty() -> {
                Text(text = "Error: $errorMessage")
            }

            recommendation == null -> {
                Text(text = "Loading recommendation...")
            }

            else -> {
                Card(modifier = Modifier.fillMaxWidth()) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text("Recommendation type: ${recommendation!!.recommendationType}")
                        Text("Intensity: ${recommendation!!.intensity}")
                        Text("Duration: ${recommendation!!.durationMinutes} min")
                        Text("Message: ${recommendation!!.message}")
                        Text("Date: ${recommendation!!.date}")
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