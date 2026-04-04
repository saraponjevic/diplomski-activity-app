package com.example.activityapp.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import com.example.activityapp.data.remote.dto.user.ChangePasswordRequest
import com.example.activityapp.data.repository.ActivityRepository
import com.example.activityapp.ui.theme.AppBackground
import com.example.activityapp.ui.theme.BlushBeet
import com.example.activityapp.ui.theme.CardBackground
import com.example.activityapp.ui.theme.Cream
import com.example.activityapp.ui.theme.SavorySage
import com.example.activityapp.ui.theme.TextPrimary
import com.example.activityapp.ui.theme.TextSecondary
import kotlinx.coroutines.launch

@Composable
fun ChangePasswordScreen(
    userId: Long,
    onBackClick: () -> Unit,
    onSaveSuccess: () -> Unit
) {
    val repository = ActivityRepository()
    val scope = rememberCoroutineScope()

    var currentPassword by remember { mutableStateOf("") }
    var newPassword by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }

    var saving by remember { mutableStateOf(false) }
    var message by remember { mutableStateOf("") }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = AppBackground
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(20.dp),
            verticalArrangement = Arrangement.Top
        ) {
            Text(
                text = "Change password",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                color = TextPrimary
            )

            Spacer(modifier = Modifier.height(18.dp))

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = CardBackground),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(
                    modifier = Modifier.padding(18.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    PasswordField(
                        value = currentPassword,
                        onValueChange = { currentPassword = it },
                        label = "Current password"
                    )

                    PasswordField(
                        value = newPassword,
                        onValueChange = { newPassword = it },
                        label = "New password"
                    )

                    PasswordField(
                        value = confirmPassword,
                        onValueChange = { confirmPassword = it },
                        label = "Confirm new password"
                    )
                }
            }

            Spacer(modifier = Modifier.height(18.dp))

            Button(
                onClick = {
                    when {
                        currentPassword.isBlank() || newPassword.isBlank() || confirmPassword.isBlank() -> {
                            message = "Please fill in all password fields."
                        }

                        newPassword != confirmPassword -> {
                            message = "New password and confirm password do not match."
                        }

                        newPassword.length < 6 -> {
                            message = "New password must have at least 6 characters."
                        }

                        else -> {
                            scope.launch {
                                try {
                                    saving = true
                                    message = ""

                                    val request = ChangePasswordRequest(
                                        currentPassword = currentPassword,
                                        newPassword = newPassword
                                    )

                                    repository.changePassword(userId, request)
                                    onSaveSuccess()
                                } catch (e: Exception) {
                                    message = e.message ?: "Password change failed"
                                } finally {
                                    saving = false
                                }
                            }
                        }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(54.dp),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = BlushBeet,
                    contentColor = Cream
                ),
                enabled = !saving
            ) {
                if (saving) {
                    CircularProgressIndicator(color = Cream)
                } else {
                    Text(
                        text = "Save password",
                        style = MaterialTheme.typography.labelLarge
                    )
                }
            }

            TextButton(
                onClick = onBackClick,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "Back",
                    color = SavorySage,
                    style = MaterialTheme.typography.labelLarge
                )
            }

            if (message.isNotEmpty()) {
                Spacer(modifier = Modifier.height(10.dp))
                Text(
                    text = message,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}

@Composable
private fun PasswordField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = Modifier.fillMaxWidth(),
        label = {
            Text(
                text = label,
                style = MaterialTheme.typography.bodyMedium
            )
        },
        visualTransformation = PasswordVisualTransformation(),
        singleLine = true,
        shape = RoundedCornerShape(16.dp),
        colors = OutlinedTextFieldDefaults.colors(
            focusedContainerColor = Cream,
            unfocusedContainerColor = Cream,
            disabledContainerColor = Cream,
            focusedBorderColor = BlushBeet,
            unfocusedBorderColor = SavorySage.copy(alpha = 0.35f),
            focusedLabelColor = BlushBeet,
            unfocusedLabelColor = TextSecondary,
            focusedTextColor = TextPrimary,
            unfocusedTextColor = TextPrimary,
            cursorColor = BlushBeet
        ),
        textStyle = MaterialTheme.typography.bodyLarge
    )
}