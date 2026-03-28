package com.example.activityapp.ui.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.navigation.NavHostController
import kotlinx.coroutines.launch
import androidx.compose.material3.DrawerValue

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScaffoldScreen(
    title: String,
    navController: NavHostController,
    onLogoutClick: () -> Unit,
    content: @Composable () -> Unit
) {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

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
                onMotivationClick = {
                    scope.launch { drawerState.close() }
                    navController.navigate(Screen.Motivation.route)
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
                    },
                    colors = TopAppBarDefaults.centerAlignedTopAppBarColors()
                )
            }
        ) { paddingValues ->
            androidx.compose.foundation.layout.Box(
                modifier = androidx.compose.ui.Modifier.padding(paddingValues)
            ) {
                content()
            }
        }
    }
}