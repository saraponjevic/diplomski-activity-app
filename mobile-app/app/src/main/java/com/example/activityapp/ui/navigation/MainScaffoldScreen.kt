package com.example.activityapp.ui.navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.NotificationsNone
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.example.activityapp.data.remote.dto.user.UserResponse
import com.example.activityapp.data.repository.ActivityRepository
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScaffoldScreen(
    title: String,
    userId: Long,
    navController: NavHostController,
    onLogoutClick: () -> Unit,
    onNotificationsClick: () -> Unit = {},
    content: @Composable () -> Unit
) {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val repository = remember { ActivityRepository() }

    var user by remember { mutableStateOf<UserResponse?>(null) }

    LaunchedEffect(userId) {
        try {
            user = repository.getUserById(userId)
        } catch (_: Exception) {
        }
    }

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            AppDrawer(
                onDashboardClick = {
                    scope.launch { drawerState.close() }
                    navController.navigate(Screen.Dashboard.route)
                },
                onRecommendationClick = {
                    scope.launch { drawerState.close() }
                    navController.navigate(Screen.RecommendationDetails.route)
                },
                onNutritionClick = {
                    scope.launch { drawerState.close() }
                    navController.navigate(Screen.Nutrition.route)
                },
                onWellnessClick = {
                    scope.launch { drawerState.close() }
                    navController.navigate(Screen.Wellness.route)
                },
                onFreeTimeClick = {
                    scope.launch { drawerState.close() }
                    navController.navigate(Screen.FreeTime.route)
                },
                onPlannerClick = {
                    scope.launch { drawerState.close() }
                    navController.navigate(Screen.Planner.route)
                },
                onProfileClick = {
                    scope.launch { drawerState.close() }
                    navController.navigate(Screen.Profile.route)
                },
                onLogoutClick = {
                    scope.launch { drawerState.close() }
                    onLogoutClick()
                }
            )
        }
    ) {
        Scaffold(
            topBar = {
                CenterAlignedTopAppBar(
                    title = { Text(title) },
                    navigationIcon = {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            IconButton(
                                onClick = {
                                    scope.launch { drawerState.open() }
                                }
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Menu,
                                    contentDescription = "Menu"
                                )
                            }

                            IconButton(
                                onClick = onNotificationsClick
                            ) {
                                Icon(
                                    imageVector = Icons.Default.NotificationsNone,
                                    contentDescription = "Notifications"
                                )
                            }
                        }
                    },
                    actions = {
                        TopBarProfileAvatar(
                            profileImageUrl = user?.profileImageUrl,
                            onClick = {
                                navController.navigate(Screen.Profile.route)
                            }
                        )
                    },
                    colors = TopAppBarDefaults.centerAlignedTopAppBarColors()
                )
            }
        ) { paddingValues ->
            Box(
                modifier = Modifier.padding(paddingValues)
            ) {
                content()
            }
        }
    }
}

@Composable
private fun TopBarProfileAvatar(
    profileImageUrl: String?,
    onClick: () -> Unit
) {
    val imageUrl = profileImageUrl?.let { "http://10.0.2.2:8080$it" }

    if (!imageUrl.isNullOrBlank()) {
        AsyncImage(
            model = imageUrl,
            contentDescription = "Profile",
            modifier = Modifier
                .padding(end = 12.dp)
                .size(36.dp)
                .clip(CircleShape)
                .clickable { onClick() },
            contentScale = ContentScale.Crop
        )
    } else {
        Box(
            modifier = Modifier
                .padding(end = 12.dp)
                .size(36.dp)
                .clip(CircleShape)
                .background(androidx.compose.material3.MaterialTheme.colorScheme.primaryContainer)
                .clickable { onClick() },
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.Person,
                contentDescription = "Profile",
                tint = androidx.compose.material3.MaterialTheme.colorScheme.onPrimaryContainer
            )
        }
    }
}