package com.example.activityapp.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.activityapp.data.remote.dto.RegisterUserRequest
import com.example.activityapp.data.repository.ActivityRepository
import kotlinx.coroutines.launch
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll

@Composable
fun RegisterScreen(
    onRegisterSuccess: (Long) -> Unit,
    onBackToLoginClick: () -> Unit
) {
    val firstName = remember { mutableStateOf("") }
    val lastName = remember { mutableStateOf("") }
    val email = remember { mutableStateOf("") }
    val password = remember { mutableStateOf("") }
    val age = remember { mutableStateOf("") }
    val height = remember { mutableStateOf("") }
    val weight = remember { mutableStateOf("") }
    val activityLevel = remember { mutableStateOf("") }
    val goal = remember { mutableStateOf("") }

    val message = remember { mutableStateOf("") }

    val repository = ActivityRepository()
    val scope = rememberCoroutineScope()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(
            text = "Register",
            style = MaterialTheme.typography.headlineMedium
        )

        OutlinedTextField(
            value = firstName.value,
            onValueChange = { firstName.value = it },
            modifier = Modifier.fillMaxWidth(),
            label = { Text("First name") }
        )

        OutlinedTextField(
            value = lastName.value,
            onValueChange = { lastName.value = it },
            modifier = Modifier.fillMaxWidth(),
            label = { Text("Last name") }
        )

        OutlinedTextField(
            value = email.value,
            onValueChange = { email.value = it },
            modifier = Modifier.fillMaxWidth(),
            label = { Text("Email") }
        )

        OutlinedTextField(
            value = password.value,
            onValueChange = { password.value = it },
            modifier = Modifier.fillMaxWidth(),
            label = { Text("Password") }
        )

        OutlinedTextField(
            value = age.value,
            onValueChange = { age.value = it },
            modifier = Modifier.fillMaxWidth(),
            label = { Text("Age") }
        )

        OutlinedTextField(
            value = height.value,
            onValueChange = { height.value = it },
            modifier = Modifier.fillMaxWidth(),
            label = { Text("Height") }
        )

        OutlinedTextField(
            value = weight.value,
            onValueChange = { weight.value = it },
            modifier = Modifier.fillMaxWidth(),
            label = { Text("Weight") }
        )

        OutlinedTextField(
            value = activityLevel.value,
            onValueChange = { activityLevel.value = it },
            modifier = Modifier.fillMaxWidth(),
            label = { Text("Activity level") }
        )

        OutlinedTextField(
            value = goal.value,
            onValueChange = { goal.value = it },
            modifier = Modifier.fillMaxWidth(),
            label = { Text("Goal") }
        )

        Button(
            onClick = {
                scope.launch {
                    try {
                        val request = RegisterUserRequest(
                            email = email.value,
                            password = password.value,
                            firstName = firstName.value,
                            lastName = lastName.value,
                            age = age.value.toIntOrNull() ?: 0,
                            height = height.value.toDoubleOrNull() ?: 0.0,
                            weight = weight.value.toDoubleOrNull() ?: 0.0,
                            activityLevel = activityLevel.value,
                            goal = goal.value
                        )

                        val response = repository.registerUser(request)
                        message.value = "User registered successfully. ID = ${response.id}"
                        onRegisterSuccess(response.id)
                    } catch (e: Exception) {
                        message.value = "Error: ${e.message}"
                    }
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
        ) {
            Text("Register")
        }

        Button(
            onClick = onBackToLoginClick,
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
        ) {
            Text("Back to Login")
        }

        if (message.value.isNotEmpty()) {
            Text(
                text = message.value,
                modifier = Modifier.padding(top = 8.dp)
            )
        }
    }
}