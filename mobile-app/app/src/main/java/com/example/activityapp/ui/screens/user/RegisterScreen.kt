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
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme

import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.activityapp.data.remote.dto.user.RegisterUserRequest
import com.example.activityapp.data.repository.ActivityRepository
import com.example.activityapp.ui.theme.BlushBeet
import com.example.activityapp.ui.theme.OatLatte
import com.example.activityapp.ui.theme.TextPrimary
import com.example.activityapp.ui.theme.TextSecondary
import com.example.activityapp.ui.theme.WhiteSoft
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
    var passwordVisible by remember { mutableStateOf(false) }

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

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            Box(
                modifier = Modifier
                    .size(360.dp)
                    .offset(x = 120.dp, y = (-40).dp)
                    .clip(CircleShape)
                    .background(BlushBeet.copy(alpha = 0.55f))
                    .align(Alignment.TopCenter)
            )

            Box(
                modifier = Modifier
                    .size(400.dp)
                    .offset(x = (-120).dp, y = 90.dp)
                    .clip(CircleShape)
                    .background(OatLatte.copy(alpha = 0.40f))
                    .align(Alignment.BottomStart)
            )

            Box(
                modifier = Modifier
                    .size(230.dp)
                    .offset(x = 110.dp, y = 90.dp)
                    .clip(CircleShape)
                    .background(OatLatte)
                    .align(Alignment.BottomEnd)
            )

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 24.dp, vertical = 24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.height(46.dp))

                Text(
                    text = "Join Berrily",
                    style = MaterialTheme.typography.bodyLarge,
                    color = TextSecondary
                )

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = "Create Account",
                    style = MaterialTheme.typography.headlineLarge,
                    color = TextPrimary
                )

                Spacer(modifier = Modifier.height(10.dp))

                Text(
                    text = "Step $currentStep of 4",
                    style = MaterialTheme.typography.bodyMedium,
                    color = TextSecondary
                )

                Spacer(modifier = Modifier.height(12.dp))

                LinearProgressIndicator(
                    progress = { progress },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(8.dp)
                        .clip(RoundedCornerShape(99.dp)),
                    color = BlushBeet,
                    trackColor = BlushBeet.copy(alpha = 0.18f)
                )

                Spacer(modifier = Modifier.height(24.dp))

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .shadow(
                            elevation = 18.dp,
                            shape = RoundedCornerShape(32.dp),
                            ambientColor = BlushBeet.copy(alpha = 0.16f),
                            spotColor = BlushBeet.copy(alpha = 0.16f)
                        ),
                    shape = RoundedCornerShape(32.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surface
                    ),
                    elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 18.dp, vertical = 22.dp),
                        verticalArrangement = Arrangement.spacedBy(14.dp)
                    ) {
                        when (currentStep) {
                            1 -> {
                                Text(
                                    text = "Basic information",
                                    style = MaterialTheme.typography.titleLarge,
                                    color = TextPrimary
                                )

                                AppTextField(
                                    value = firstName,
                                    onValueChange = { firstName = it },
                                    label = "First name"
                                )

                                AppTextField(
                                    value = lastName,
                                    onValueChange = { lastName = it },
                                    label = "Last name"
                                )

                                AppTextField(
                                    value = email,
                                    onValueChange = { email = it },
                                    label = "Email"
                                )

                                OutlinedTextField(
                                    value = password,
                                    onValueChange = { password = it },
                                    modifier = Modifier.fillMaxWidth(),
                                    label = {
                                        Text(
                                            text = "Password",
                                            style = MaterialTheme.typography.bodyMedium
                                        )
                                    },
                                    singleLine = true,
                                    shape = RoundedCornerShape(22.dp),
                                    visualTransformation = if (passwordVisible) {
                                        VisualTransformation.None
                                    } else {
                                        PasswordVisualTransformation()
                                    },
                                    trailingIcon = {
                                        IconButton(onClick = { passwordVisible = !passwordVisible }) {
                                            Icon(
                                                imageVector = if (passwordVisible) {
                                                    Icons.Default.Visibility
                                                } else {
                                                    Icons.Default.VisibilityOff
                                                },
                                                contentDescription = null,
                                                tint = TextSecondary
                                            )
                                        }
                                    },
                                    colors = appOutlinedTextFieldColors()
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
                                        .height(58.dp),
                                    shape = RoundedCornerShape(24.dp),
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = MaterialTheme.colorScheme.primary,
                                        contentColor = MaterialTheme.colorScheme.onPrimary
                                    )
                                ) {
                                    Text(
                                        text = "Next",
                                        style = MaterialTheme.typography.titleLarge
                                    )
                                }

                                TextButton(
                                    onClick = onBackToLoginClick,
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Text(
                                        text = "Back to Login",
                                        style = MaterialTheme.typography.bodyMedium,
                                        color = TextSecondary
                                    )
                                }
                            }

                            2 -> {
                                Text(
                                    text = "Body information",
                                    style = MaterialTheme.typography.titleLarge,
                                    color = TextPrimary
                                )

                                AppTextField(
                                    value = age,
                                    onValueChange = { age = it.filter { ch -> ch.isDigit() } },
                                    label = "Age"
                                )

                                AppTextField(
                                    value = height,
                                    onValueChange = { newValue ->
                                        height = newValue.filter { it.isDigit() || it == '.' }
                                    },
                                    label = "Height (cm)"
                                )

                                AppTextField(
                                    value = weight,
                                    onValueChange = { newValue ->
                                        weight = newValue.filter { it.isDigit() || it == '.' }
                                    },
                                    label = "Weight (kg)"
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
                                        .height(58.dp),
                                    shape = RoundedCornerShape(24.dp),
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = MaterialTheme.colorScheme.primary,
                                        contentColor = MaterialTheme.colorScheme.onPrimary
                                    )
                                ) {
                                    Text(
                                        text = "Next",
                                        style = MaterialTheme.typography.titleLarge
                                    )
                                }

                                TextButton(
                                    onClick = {
                                        message = ""
                                        currentStep = 1
                                    },
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Text(
                                        text = "Back",
                                        style = MaterialTheme.typography.bodyMedium,
                                        color = TextSecondary
                                    )
                                }
                            }

                            3 -> {
                                Text(
                                    text = "Preferences",
                                    style = MaterialTheme.typography.titleLarge,
                                    color = TextPrimary
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

                                AppTextField(
                                    value = goalSteps,
                                    onValueChange = { goalSteps = it.filter { ch -> ch.isDigit() } },
                                    label = "Daily step goal"
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
                                        .height(58.dp),
                                    shape = RoundedCornerShape(24.dp),
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = MaterialTheme.colorScheme.primary,
                                        contentColor = MaterialTheme.colorScheme.onPrimary
                                    )
                                ) {
                                    Text(
                                        text = "Next",
                                        style = MaterialTheme.typography.titleLarge
                                    )
                                }

                                TextButton(
                                    onClick = {
                                        message = ""
                                        currentStep = 2
                                    },
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Text(
                                        text = "Back",
                                        style = MaterialTheme.typography.bodyMedium,
                                        color = TextSecondary
                                    )
                                }
                            }

                            4 -> {
                                Text(
                                    text = "Profile image",
                                    style = MaterialTheme.typography.titleLarge,
                                    color = TextPrimary
                                )

                                Text(
                                    text = "You can add a profile image now or skip this step.",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = TextSecondary
                                )

                                Spacer(modifier = Modifier.height(12.dp))

                                selectedImageUri?.let { uri ->
                                    AsyncImage(
                                        model = uri,
                                        contentDescription = "Selected profile image",
                                        modifier = Modifier
                                            .size(120.dp)
                                            .clip(CircleShape)
                                            .align(Alignment.CenterHorizontally),
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
                                        .height(58.dp),
                                    shape = RoundedCornerShape(24.dp),
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = BlushBeet,
                                        contentColor = TextPrimary
                                    )
                                ) {
                                    Text(
                                        text = "Choose image",
                                        style = MaterialTheme.typography.titleMedium
                                    )
                                }

                                TextButton(
                                    onClick = {
                                        selectedImageUri = null
                                    },
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Text(
                                        text = "Remove selected image",
                                        style = MaterialTheme.typography.bodyMedium,
                                        color = TextSecondary
                                    )
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
                                        .height(58.dp),
                                    shape = RoundedCornerShape(24.dp),
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = MaterialTheme.colorScheme.primary,
                                        contentColor = MaterialTheme.colorScheme.onPrimary
                                    )
                                ) {
                                    Text(
                                        text = "Finish registration",
                                        style = MaterialTheme.typography.titleLarge
                                    )
                                }

                                TextButton(
                                    onClick = {
                                        currentStep = 3
                                    },
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Text(
                                        text = "Back",
                                        style = MaterialTheme.typography.bodyMedium,
                                        color = TextSecondary
                                    )
                                }
                            }
                        }

                        if (message.isNotEmpty()) {
                            Text(
                                text = message,
                                style = MaterialTheme.typography.bodyMedium,
                                color = TextPrimary
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))
            }
        }
    }
}

@Composable
private fun AppTextField(
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
        shape = RoundedCornerShape(22.dp),
        colors = appOutlinedTextFieldColors()
    )
}

@Composable
private fun appOutlinedTextFieldColors() = OutlinedTextFieldDefaults.colors(
    focusedContainerColor = WhiteSoft,
    unfocusedContainerColor = WhiteSoft,
    focusedBorderColor = BlushBeet.copy(alpha = 0.9f),
    unfocusedBorderColor = BlushBeet.copy(alpha = 0.22f),
    focusedTextColor = TextPrimary,
    unfocusedTextColor = TextPrimary,
    focusedLabelColor = TextSecondary,
    unfocusedLabelColor = TextSecondary,
    cursorColor = MaterialTheme.colorScheme.primary
)

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
            shape = RoundedCornerShape(22.dp),
            colors = appOutlinedTextFieldColors()
        )

        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },

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