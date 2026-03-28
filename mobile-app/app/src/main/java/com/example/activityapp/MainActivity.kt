package com.example.activityapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.*
import com.example.activityapp.ui.navigation.AppNavigation

import com.example.activityapp.ui.screens.LoginScreen


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            var currentUserId by remember { mutableStateOf<Long?>(null) }

            if (currentUserId == null) {
                LoginScreen(
                    onLoginSuccess = { userId ->
                        currentUserId = userId
                    },
                    onGoToRegisterClick = {}
                )
            } else {
                AppNavigation(
                    userId = currentUserId!!,
                    onLogoutClick = {
                        currentUserId = null
                    }
                )
            }
        }
    }
}