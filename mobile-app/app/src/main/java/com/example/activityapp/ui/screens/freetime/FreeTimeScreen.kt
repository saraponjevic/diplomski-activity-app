package com.example.activityapp.ui.screens.freetime

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
fun FreeTimeScreen(
    userId: Long,
    modifier: Modifier = Modifier,
    dashboardViewModel: DashboardViewModel = viewModel()
) {
    val latestRecommendation by dashboardViewModel.latestRecommendation.collectAsState()
    val errorMessage by dashboardViewModel.errorMessage.collectAsState()

    LaunchedEffect(userId) {
        dashboardViewModel.loadDashboardData(userId)
    }

    val freeTime = latestRecommendation?.freeTime

    if (freeTime == null) {
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Text(
                text = "Free Time",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = errorMessage ?: "No free time recommendations available yet."
            )
        }
        return
    }

    val sortedGroups = freeTime.categoryGroups
        .sortedBy { getFreeTimeCategoryOrder(it.category) }

    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(vertical = 16.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        item {
            Column(
                modifier = Modifier.padding(horizontal = 16.dp)
            ) {
                Text(
                    text = "Free Time",
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = freeTime.headline,
                    style = MaterialTheme.typography.bodyLarge
                )
            }
        }

        item {
            FreeTimeMainSuggestionCard(
                text = freeTime.mainSuggestion,
                modifier = Modifier.padding(horizontal = 16.dp)
            )
        }

        items(sortedGroups) { group ->
            FreeTimeCategorySection(group = group)
        }

        item {
            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}