package com.example.activityapp.ui.navigation

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun AppDrawer(
    onDashboardClick: () -> Unit,
    onRecommendationClick: () -> Unit,
    onNutritionClick: () -> Unit,
    onWellnessClick: () -> Unit,
    onFreeTimeClick: () -> Unit,
    onPlannerClick: () -> Unit,
    onProfileClick: () -> Unit,
    onLogoutClick: () -> Unit
) {
    ModalDrawerSheet {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = "Activity App",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )

            HorizontalDivider()

            DrawerItem("Dashboard", onDashboardClick)
            DrawerItem("Daily Recommendation", onRecommendationClick)
            DrawerItem("Nutrition", onNutritionClick)
            DrawerItem("Wellness", onWellnessClick)
            DrawerItem("Free Time", onFreeTimeClick)
            DrawerItem("Planner", onPlannerClick)
            DrawerItem("Profile", onProfileClick)

            Spacer(modifier = Modifier.weight(1f))

            HorizontalDivider()
            DrawerItem("Logout", onLogoutClick)
        }
    }
}

@Composable
fun DrawerItem(
    title: String,
    onClick: () -> Unit
) {
    Text(
        text = title,
        style = MaterialTheme.typography.titleMedium,
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(vertical = 12.dp)
    )
}