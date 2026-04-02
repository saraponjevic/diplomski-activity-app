package com.example.activityapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.*
import com.example.activityapp.ui.navigation.AppNavigation
import com.example.activityapp.ui.screens.user.LoginScreen
import com.example.activityapp.ui.screens.user.RegisterScreen
import com.example.activityapp.ui.theme.ActivityAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {

            ActivityAppTheme {
            var currentUserId by remember { mutableStateOf<Long?>(null) }
            var authScreen by remember { mutableStateOf("login") }

            if (currentUserId == null) {
                when (authScreen) {
                    "login" -> LoginScreen(
                        onLoginSuccess = { userId ->
                            currentUserId = userId
                        },
                        onGoToRegisterClick = {
                            authScreen = "register"
                        }
                    )

                    "register" -> RegisterScreen(
                        onRegisterSuccess = { userId ->
                            currentUserId = userId
                        },
                        onBackToLoginClick = {
                            authScreen = "login"
                        }
                    )
                }
            } else {
                AppNavigation(
                    userId = currentUserId!!,
                    onLogoutClick = {
                        currentUserId = null
                        authScreen = "login"
                    }
                )
            }
        }
        }
    }

}