package com.example.activityapp.ui.screens.freetime

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.activityapp.ui.theme.AppBackground
import com.example.activityapp.ui.theme.AvocadoSmoothie
import com.example.activityapp.ui.theme.BlushBeet
import com.example.activityapp.ui.theme.TextPrimary
import com.example.activityapp.ui.theme.TextSecondary
import com.example.activityapp.ui.theme.WhiteSoft
import com.example.activityapp.ui.viewmodel.DashboardViewModel

@Composable
fun FreeTimeScreen(
    userId: Long,
    onBack: () -> Unit,
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
                .background(AppBackground)
                .padding(horizontal = 16.dp, vertical = 20.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = onBack,
                    modifier = Modifier.size(40.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.ArrowBackIosNew,
                        contentDescription = "Back",
                        tint = TextPrimary
                    )
                }

                Spacer(modifier = Modifier.width(8.dp))

                Text(
                    text = "Free Time",
                    style = MaterialTheme.typography.titleLarge,
                    color = TextPrimary,
                    fontWeight = FontWeight.SemiBold
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(
                    containerColor = WhiteSoft
                ),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Text(
                    text = errorMessage ?: "No free time recommendations available yet.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = TextSecondary,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp)
                )
            }
        }
        return
    }

    val sortedGroups = freeTime.categoryGroups
        .sortedBy { getFreeTimeCategoryOrder(it.category) }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(AppBackground),
        contentPadding = PaddingValues(top = 20.dp, bottom = 24.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        item {
            Column(
                modifier = Modifier.padding(horizontal = 16.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(
                        onClick = onBack,
                        modifier = Modifier.size(40.dp)
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = TextPrimary
                        )
                    }

                    Spacer(modifier = Modifier.width(8.dp))

                    Text(
                        text = "Free Time",
                        style = MaterialTheme.typography.titleLarge,
                        color = TextPrimary,
                        fontWeight = FontWeight.SemiBold
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                Card(
                    shape = RoundedCornerShape(22.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = AvocadoSmoothie.copy(alpha = 0.25f)
                    ),
                    elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
                ) {
                    Text(
                        text = freeTime.headline,
                        style = MaterialTheme.typography.bodyMedium,
                        color = TextPrimary,
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp)
                    )
                }
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
            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}