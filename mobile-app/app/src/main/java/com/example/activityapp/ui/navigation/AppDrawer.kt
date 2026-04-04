package com.example.activityapp.ui.navigation

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.CalendarMonth
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Logout
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Restaurant
import androidx.compose.material.icons.outlined.SelfImprovement
import androidx.compose.material.icons.outlined.Spa
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.graphics.vector.ImageVector
import com.example.activityapp.R
import com.example.activityapp.ui.theme.TextPrimary
import com.example.activityapp.ui.theme.TextSecondary

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

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 0.dp)
                    .offset(x = (-10).dp, y = 12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = painterResource(id = R.drawable.berrily4),
                    contentDescription = "Berrily logo",
                    modifier = Modifier
                        .height(115.dp),
                    contentScale = ContentScale.Fit
                )
            }

            HorizontalDivider()

            DrawerItem("Dashboard", Icons.Outlined.Home, onDashboardClick)
            DrawerItem("Nutrition", Icons.Outlined.Restaurant, onNutritionClick)
            DrawerItem("Wellness", Icons.Outlined.Spa, onWellnessClick)
            DrawerItem("Free Time", Icons.Outlined.SelfImprovement, onFreeTimeClick)
            DrawerItem("Planner", Icons.Outlined.CalendarMonth, onPlannerClick)
            DrawerItem("Profile", Icons.Outlined.Person, onProfileClick)

            Spacer(modifier = Modifier.weight(1f))

            HorizontalDivider()
            DrawerItem("Logout", Icons.Outlined.Logout, onLogoutClick)
        }
    }
}

@Composable
fun DrawerItem(
    title: String,
    icon: ImageVector,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = title,
            tint = TextSecondary,
            modifier = Modifier.size(20.dp)
        )

        Spacer(modifier = Modifier.width(12.dp))

        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium,
            color = TextPrimary
        )
    }
}