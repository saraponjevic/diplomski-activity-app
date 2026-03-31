package com.example.activityapp.ui.screens.user

import android.content.Intent
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.activityapp.data.remote.dto.user.RegisterUserRequest
import com.example.activityapp.data.repository.ActivityRepository
import com.example.activityapp.util.createImagePartFromUri
import kotlinx.coroutines.launch

data class DropdownOption(
    val label: String,
    val value: String
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterScreen(
    onRegisterSuccess: (Long) -> Unit,
    onBackToLoginClick: () -> Unit
) {
    var firstName by remember { mutableStateOf("") }
    var lastName by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var age by remember { mutableStateOf("") }
    var height by remember { mutableStateOf("") }
    var weight by remember { mutableStateOf("") }
    var activityLevel by remember { mutableStateOf("") }
    var goal by remember { mutableStateOf("") }
    var goalSteps by remember { mutableStateOf("") }

    var message by remember { mutableStateOf("") }
    var currentStep by remember { mutableIntStateOf(1) }

    val repository = ActivityRepository()
    val scope = rememberCoroutineScope()

    val activityOptions = listOf(
        DropdownOption("Low", "LOW"),
        DropdownOption("Medium", "MEDIUM"),
        DropdownOption("High", "HIGH")
    )

    val goalOptions = listOf(
        DropdownOption("Increase activity", "INCREASE_ACTIVITY"),
        DropdownOption("Maintain condition", "MAINTAIN_CONDITION"),
        DropdownOption("Build habit", "BUILD_HABIT")
    )

    val progress = currentStep / 4f

    val context = LocalContext.current
    var selectedImageUri by remember { mutableStateOf<Uri?>(null) }

    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.OpenDocument()
    ) { uri: Uri? ->
        uri?.let {
            context.contentResolver.takePersistableUriPermission(
                it,
                Intent.FLAG_GRANT_READ_URI_PERMISSION
            )
        }
        selectedImageUri = uri
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(20.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Create account",
                style = MaterialTheme.typography.headlineMedium
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Step $currentStep of 4",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(10.dp))

            LinearProgressIndicator(
                progress = { progress },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(8.dp),
            )

            Spacer(modifier = Modifier.height(20.dp))

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(24.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
            ) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    when (currentStep) {
                        1 -> {
                            Text(
                                text = "Basic information",
                                style = MaterialTheme.typography.titleMedium
                            )

                            OutlinedTextField(
                                value = firstName,
                                onValueChange = { firstName = it },
                                modifier = Modifier.fillMaxWidth(),
                                label = { Text("First name") },
                                singleLine = true
                            )

                            OutlinedTextField(
                                value = lastName,
                                onValueChange = { lastName = it },
                                modifier = Modifier.fillMaxWidth(),
                                label = { Text("Last name") },
                                singleLine = true
                            )

                            OutlinedTextField(
                                value = email,
                                onValueChange = { email = it },
                                modifier = Modifier.fillMaxWidth(),
                                label = { Text("Email") },
                                singleLine = true
                            )

                            OutlinedTextField(
                                value = password,
                                onValueChange = { password = it },
                                modifier = Modifier.fillMaxWidth(),
                                label = { Text("Password") },
                                visualTransformation = PasswordVisualTransformation(),
                                singleLine = true
                            )

                            Spacer(modifier = Modifier.height(4.dp))

                            Button(
                                onClick = {
                                    if (
                                        firstName.isBlank() ||
                                        lastName.isBlank() ||
                                        email.isBlank() ||
                                        password.isBlank()
                                    ) {
                                        message = "Please fill in all fields in step 1."
                                    } else {
                                        message = ""
                                        currentStep = 2
                                    }
                                },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(52.dp),
                                shape = RoundedCornerShape(16.dp)
                            ) {
                                Text("Next")
                            }

                            TextButton(
                                onClick = onBackToLoginClick,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text("Back to Login")
                            }
                        }

                        2 -> {
                            Text(
                                text = "Body information",
                                style = MaterialTheme.typography.titleMedium
                            )

                            OutlinedTextField(
                                value = age,
                                onValueChange = { age = it.filter { ch -> ch.isDigit() } },
                                modifier = Modifier.fillMaxWidth(),
                                label = { Text("Age") },
                                singleLine = true
                            )

                            OutlinedTextField(
                                value = height,
                                onValueChange = { newValue ->
                                    height = newValue.filter { it.isDigit() || it == '.' }
                                },
                                modifier = Modifier.fillMaxWidth(),
                                label = { Text("Height (cm)") },
                                singleLine = true
                            )

                            OutlinedTextField(
                                value = weight,
                                onValueChange = { newValue ->
                                    weight = newValue.filter { it.isDigit() || it == '.' }
                                },
                                modifier = Modifier.fillMaxWidth(),
                                label = { Text("Weight (kg)") },
                                singleLine = true
                            )

                            Spacer(modifier = Modifier.height(4.dp))

                            Button(
                                onClick = {
                                    if (
                                        age.isBlank() ||
                                        height.isBlank() ||
                                        weight.isBlank()
                                    ) {
                                        message = "Please fill in all fields in step 2."
                                    } else {
                                        message = ""
                                        currentStep = 3
                                    }
                                },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(52.dp),
                                shape = RoundedCornerShape(16.dp)
                            ) {
                                Text("Next")
                            }

                            TextButton(
                                onClick = {
                                    message = ""
                                    currentStep = 1
                                },
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text("Back")
                            }
                        }

                        3 -> {
                            Text(
                                text = "Preferences",
                                style = MaterialTheme.typography.titleMedium
                            )

                            DropdownField(
                                label = "Activity level",
                                selectedValue = activityLevel,
                                options = activityOptions,
                                onOptionSelected = { activityLevel = it }
                            )

                            DropdownField(
                                label = "Goal",
                                selectedValue = goal,
                                options = goalOptions,
                                onOptionSelected = { goal = it }
                            )

                            OutlinedTextField(
                                value = goalSteps,
                                onValueChange = { goalSteps = it.filter { ch -> ch.isDigit() } },
                                modifier = Modifier.fillMaxWidth(),
                                label = { Text("Daily step goal") },
                                singleLine = true
                            )

                            Spacer(modifier = Modifier.height(4.dp))

                            Button(
                                onClick = {
                                    if (
                                        activityLevel.isBlank() ||
                                        goal.isBlank() ||
                                        goalSteps.isBlank()
                                    ) {
                                        message = "Please fill in all fields in step 3."
                                    } else {
                                        message = ""
                                        currentStep = 4
                                    }
                                },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(52.dp),
                                shape = RoundedCornerShape(16.dp)
                            ) {
                                Text("Next")
                            }

                            TextButton(
                                onClick = {
                                    message = ""
                                    currentStep = 2
                                },
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text("Back")
                            }
                        }

                        4 -> {
                            Text(
                                text = "Profile image",
                                style = MaterialTheme.typography.titleMedium
                            )

                            Text(
                                text = "You can add a profile image now or skip this step.",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )

                            Spacer(modifier = Modifier.height(12.dp))

                            selectedImageUri?.let { uri ->
                                AsyncImage(
                                    model = uri,
                                    contentDescription = "Selected profile image",
                                    modifier = Modifier
                                        .size(120.dp)
                                        .clip(CircleShape),
                                    contentScale = ContentScale.Crop
                                )

                                Spacer(modifier = Modifier.height(12.dp))
                            }

                            Button(
                                onClick = {
                                    imagePickerLauncher.launch(arrayOf("image/*"))
                                },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(52.dp),
                                shape = RoundedCornerShape(16.dp)
                            ) {
                                Text("Choose image")
                            }

                            TextButton(
                                onClick = {
                                    selectedImageUri = null
                                },
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text("Remove selected image")
                            }

                            Spacer(modifier = Modifier.height(8.dp))

                            Button(
                                onClick = {
                                    scope.launch {
                                        try {
                                            val request = RegisterUserRequest(
                                                email = email,
                                                password = password,
                                                firstName = firstName,
                                                lastName = lastName,
                                                age = age.toIntOrNull() ?: 0,
                                                height = height.toDoubleOrNull() ?: 0.0,
                                                weight = weight.toDoubleOrNull() ?: 0.0,
                                                activityLevel = activityLevel,
                                                goal = goal,
                                                goalSteps = goalSteps.toIntOrNull() ?: 8000
                                            )

                                            val registerResponse = repository.registerUser(request)

                                            if (selectedImageUri != null) {
                                                val imagePart = createImagePartFromUri(context, selectedImageUri!!)
                                                repository.uploadProfileImage(registerResponse.id, imagePart)
                                            }

                                            message = "User registered successfully."
                                            onRegisterSuccess(registerResponse.id)

                                        } catch (e: Exception) {
                                            message = "Error: ${e.message}"
                                        }
                                    }
                                },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(52.dp),
                                shape = RoundedCornerShape(16.dp)
                            ) {
                                Text("Finish registration")
                            }

                            TextButton(
                                onClick = {
                                    currentStep = 3
                                },
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text("Back")
                            }
                        }
                    }
                }
            }

            if (message.isNotEmpty()) {
                Spacer(modifier = Modifier.height(14.dp))
                Text(
                    text = message,
                    color = MaterialTheme.colorScheme.primary,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DropdownField(
    label: String,
    selectedValue: String,
    options: List<DropdownOption>,
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
            label = { Text(label) },
            trailingIcon = {
                ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
            },
            modifier = Modifier
                .fillMaxWidth()
                .menuAnchor()
        )

        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            options.forEach { option ->
                DropdownMenuItem(
                    text = { Text(option.label) },
                    onClick = {
                        onOptionSelected(option.value)
                        expanded = false
                    }
                )
            }
        }
    }
}