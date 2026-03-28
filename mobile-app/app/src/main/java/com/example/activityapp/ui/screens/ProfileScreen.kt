package com.example.activityapp.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.activityapp.data.remote.dto.UserResponse
import com.example.activityapp.data.repository.ActivityRepository

@Composable
fun ProfileScreen(
    userId: Long
) {
    val repository = ActivityRepository()

    var user by remember { mutableStateOf<UserResponse?>(null) }
    var errorMessage by remember { mutableStateOf("") }

    LaunchedEffect(userId) {
        try {
            user = repository.getUserById(userId)
        } catch (e: Exception) {
            errorMessage = e.message ?: "Unknown error"
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = "Profile",
            style = MaterialTheme.typography.headlineMedium
        )

        when {
            errorMessage.isNotEmpty() -> {
                Text(text = "Error: $errorMessage")
            }

            user == null -> {
                Text(text = "Loading profile...")
            }

            else -> {
                Card(modifier = Modifier.fillMaxWidth()) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text("First name: ${user!!.firstName}")
                        Text("Last name: ${user!!.lastName}")
                        Text("Email: ${user!!.email}")
                        Text("Age: ${user!!.age}")
                        Text("Height: ${user!!.height}")
                        Text("Weight: ${user!!.weight}")
                        Text("Activity level: ${user!!.activityLevel}")
                        Text("Goal: ${user!!.goal}")
                    }
                }
            }
        }
    }
}