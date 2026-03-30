package com.example.activityapp.ui.screens

import androidx.compose.runtime.*
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.activityapp.ui.screens.wellnesss.WellnessMoodSelectionScreen
import com.example.activityapp.ui.screens.wellnesss.WellnessResultScreen
import com.example.activityapp.ui.viewmodel.DashboardViewModel

@Composable
fun WellnessScreen(
    userId: Long,
    onBackToPrevious: () -> Unit,
    dashboardViewModel: DashboardViewModel = viewModel()
) {
    val wellnessDetails by dashboardViewModel.wellnessDetails.collectAsState()
    val hasSelectedMood by dashboardViewModel.hasSelectedMood.collectAsState()

    LaunchedEffect(userId) {
        dashboardViewModel.loadTodayWellness(userId)
    }

    if (!hasSelectedMood) {
        WellnessMoodSelectionScreen(
            onMoodSelected = { mood ->
                dashboardViewModel.selectMood(userId, mood)
            }
        )
    } else {
        WellnessResultScreen(
            userId = userId,
            selectedMood = wellnessDetails?.mood ?: "",
            dashboardViewModel = dashboardViewModel,
            onBack = {
                onBackToPrevious()
            }
        )
    }
}