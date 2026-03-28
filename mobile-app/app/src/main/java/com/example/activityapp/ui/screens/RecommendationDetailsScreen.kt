package com.example.activityapp.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
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
import com.example.activityapp.ui.viewmodel.DashboardViewModel

@Composable
fun RecommendationDetailsScreen(
    userId: Long,
    dashboardViewModel: DashboardViewModel = viewModel()
) {
    val recommendation by dashboardViewModel.latestRecommendation.collectAsState()

    LaunchedEffect(userId) {
        dashboardViewModel.loadDashboardData(userId)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = "Daily Recommendation",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold
        )

        Card(modifier = Modifier.fillMaxWidth()) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text("Message: ${recommendation?.message ?: "No data"}")
                Text("Type: ${recommendation?.recommendationType ?: "No data"}")
                Text("Intensity: ${recommendation?.intensity ?: "No data"}")
                Text("Duration: ${recommendation?.durationMinutes ?: 0} min")
                Text("Notification: ${recommendation?.notification ?: "No data"}")
            }
        }
    }
}