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
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.activityapp.data.remote.dto.user.UpdateUserRequest
import com.example.activityapp.data.repository.ActivityRepository
import com.example.activityapp.ui.theme.AppBackground
import com.example.activityapp.ui.theme.BlushBeet
import com.example.activityapp.ui.theme.CardBackground
import com.example.activityapp.ui.theme.Cream
import com.example.activityapp.ui.theme.SavorySage
import com.example.activityapp.ui.theme.TextPrimary
import com.example.activityapp.ui.theme.TextSecondary
import kotlinx.coroutines.launch

data class EditDropdownOption(
    val label: String,
    val value: String
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditProfileScreen(
    userId: Long,
    onBackClick: () -> Unit,
    onSaveSuccess: () -> Unit
) {
    val repository = ActivityRepository()
    val scope = rememberCoroutineScope()

    var firstName by remember { mutableStateOf("") }
    var lastName by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var age by remember { mutableStateOf("") }
    var height by remember { mutableStateOf("") }
    var weight by remember { mutableStateOf("") }
    var activityLevel by remember { mutableStateOf("") }
    var goal by remember { mutableStateOf("") }
    var goalSteps by remember { mutableStateOf("") }

    var loading by remember { mutableStateOf(true) }
    var saving by remember { mutableStateOf(false) }
    var message by remember { mutableStateOf("") }

    val activityOptions = listOf(
        EditDropdownOption("Low", "LOW"),
        EditDropdownOption("Medium", "MEDIUM"),
        EditDropdownOption("High", "HIGH")
    )

    val goalOptions = listOf(
        EditDropdownOption("Increase activity", "INCREASE_ACTIVITY"),
        EditDropdownOption("Maintain condition", "MAINTAIN_CONDITION"),
        EditDropdownOption("Build habit", "BUILD_HABIT")
    )

    LaunchedEffect(userId) {
        try {
            val user = repository.getUserById(userId)
            firstName = user.firstName
            lastName = user.lastName
            email = user.email
            age = user.age.toString()
            height = user.height.toString()
            weight = user.weight.toString()
            activityLevel = user.activityLevel
            goal = user.goal
            goalSteps = user.goalSteps.toString()
            message = ""
        } catch (e: Exception) {
            message = e.message ?: "Failed to load profile"
        } finally {
            loading = false
        }
    }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = AppBackground
    ) {
        if (loading) {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center
            ) {
                CircularProgressIndicator(
                    modifier = Modifier.padding(start = 24.dp),
                    color = SavorySage
                )
            }
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(20.dp)
            ) {
                Text(
                    text = "Edit profile",
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
                        StyledOutlinedField(
                            value = firstName,
                            onValueChange = { firstName = it },
                            label = "First name"
                        )

                        StyledOutlinedField(
                            value = lastName,
                            onValueChange = { lastName = it },
                            label = "Last name"
                        )

                        StyledOutlinedField(
                            value = email,
                            onValueChange = { email = it },
                            label = "Email"
                        )

                        StyledOutlinedField(
                            value = age,
                            onValueChange = { age = it.filter { ch -> ch.isDigit() } },
                            label = "Age"
                        )

                        StyledOutlinedField(
                            value = height,
                            onValueChange = { newValue ->
                                height = newValue.filter { it.isDigit() || it == '.' }
                            },
                            label = "Height (cm)"
                        )

                        StyledOutlinedField(
                            value = weight,
                            onValueChange = { newValue ->
                                weight = newValue.filter { it.isDigit() || it == '.' }
                            },
                            label = "Weight (kg)"
                        )

                        EditDropdownField(
                            label = "Activity level",
                            selectedValue = activityLevel,
                            options = activityOptions,
                            onOptionSelected = { activityLevel = it }
                        )

                        EditDropdownField(
                            label = "Goal",
                            selectedValue = goal,
                            options = goalOptions,
                            onOptionSelected = { goal = it }
                        )

                        StyledOutlinedField(
                            value = goalSteps,
                            onValueChange = { goalSteps = it.filter { ch -> ch.isDigit() } },
                            label = "Daily step goal"
                        )
                    }
                }

                Spacer(modifier = Modifier.height(18.dp))

                Button(
                    onClick = {
                        scope.launch {
                            try {
                                saving = true
                                message = ""

                                val request = UpdateUserRequest(
                                    email = email,
                                    firstName = firstName,
                                    lastName = lastName,
                                    age = age.toIntOrNull() ?: 0,
                                    height = height.toDoubleOrNull() ?: 0.0,
                                    weight = weight.toDoubleOrNull() ?: 0.0,
                                    activityLevel = activityLevel,
                                    goal = goal,
                                )

                                repository.updateUser(userId, request)
                                onSaveSuccess()
                            } catch (e: Exception) {
                                message = e.message ?: "Update failed"
                            } finally {
                                saving = false
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
                            text = "Save changes",
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
}

@Composable
private fun StyledOutlinedField(
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun EditDropdownField(
    label: String,
    selectedValue: String,
    options: List<EditDropdownOption>,
    onOptionSelected: (String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    val selectedLabel = options.find { it.value == selectedValue }?.label ?: ""

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded }
    ) {
        OutlinedTextField(
            value = selectedLabel,
            onValueChange = {},
            readOnly = true,
            label = {
                Text(
                    text = label,
                    style = MaterialTheme.typography.bodyMedium
                )
            },
            trailingIcon = {
                ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
            },
            modifier = Modifier
                .fillMaxWidth()
                .menuAnchor(),
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

        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            //containerColor = CardBackground
        ) {
            options.forEach { option ->
                DropdownMenuItem(
                    text = {
                        Text(
                            text = option.label,
                            style = MaterialTheme.typography.bodyMedium,
                            color = TextPrimary
                        )
                    },
                    onClick = {
                        onOptionSelected(option.value)
                        expanded = false
                    }
                )
            }
        }
    }
}