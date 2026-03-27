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
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.activityapp.data.repository.FakeActivityRepository

@Composable
fun DashboardScreen(
    onWeeklyStatsClick: () -> Unit,
    onRecommendationsClick: () -> Unit,
    onProfileClick: () -> Unit,
    onLogoutClick: () -> Unit
) {
    val repository = FakeActivityRepository()
    val dailyActivity = repository.getDailyActivity()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = "Daily Activity",
            style = MaterialTheme.typography.headlineMedium
        )

        Card(
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(text = "Steps today: ${dailyActivity.stepsToday}")
                Text(text = "Daily goal: ${dailyActivity.dailyGoal}")
                Text(text = "Goal reached: ${dailyActivity.goalPercentage}%")
                Text(text = "Activity level: ${dailyActivity.activityLevel}")
                Text(text = "Recommendation: ${dailyActivity.shortRecommendation}")
            }
        }

        Button(
            onClick = onWeeklyStatsClick,
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
        ) {
            Text("Weekly Stats")
        }

        Button(
            onClick = onRecommendationsClick,
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
        ) {
            Text("Recommendations")
        }

        Button(
            onClick = onProfileClick,
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
        ) {
            Text("Profile")
        }

        Button(
            onClick = onLogoutClick,
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
        ) {
            Text("Logout")
        }
    }
}