package com.example.activityapp.ui.screens.user

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import com.example.activityapp.R
import com.example.activityapp.data.remote.dto.user.LoginUserRequest
import com.example.activityapp.data.repository.ActivityRepository
import com.example.activityapp.ui.theme.BlushBeet
import com.example.activityapp.ui.theme.Cream
import com.example.activityapp.ui.theme.OatLatte
import com.example.activityapp.ui.theme.WhiteSoft
import kotlinx.coroutines.launch

@Composable
fun LoginScreen(
    onLoginSuccess: (Long) -> Unit,
    onGoToRegisterClick: () -> Unit
) {
    val email = remember { mutableStateOf("") }
    val password = remember { mutableStateOf("") }
    val message = remember { mutableStateOf("") }

    var passwordVisible by remember { mutableStateOf(false) }

    val repository = ActivityRepository()
    val scope = rememberCoroutineScope()

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            // Gornji blush beet oblik
            Box(
                modifier = Modifier
                    .size(360.dp)
                    .offset(x = 120.dp, y = (-40).dp)
                    .clip(CircleShape)
                    .background(BlushBeet.copy(alpha = 0.55f))
                    .align(Alignment.TopCenter)
            )

            // Donji levi krug
            Box(
                modifier = Modifier
                    .size(400.dp)
                    .offset(x = (-120).dp, y = 70.dp)
                    .clip(CircleShape)
                    .background(OatLatte.copy(alpha = 0.4f))
                    .align(Alignment.BottomStart)
            )

            // Donji desni krug
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
                Spacer(modifier = Modifier.height(52.dp))

                Text(
                    text = "Welcome back",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.9f)
                )

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = "Log In",
                    style = MaterialTheme.typography.headlineLarge,
                    color = MaterialTheme.colorScheme.onBackground
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
                        OutlinedTextField(
                            value = email.value,
                            onValueChange = { email.value = it },
                            modifier = Modifier.fillMaxWidth(),
                            label = {
                                Text(
                                    text = "Email",
                                    style = MaterialTheme.typography.bodyMedium
                                )
                            },
                            singleLine = true,
                            shape = RoundedCornerShape(22.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedContainerColor = WhiteSoft,
                                unfocusedContainerColor = WhiteSoft,
                                focusedBorderColor = BlushBeet.copy(alpha = 0.9f),
                                unfocusedBorderColor = BlushBeet.copy(alpha = 0.22f),
                                cursorColor = MaterialTheme.colorScheme.primary
                            )
                        )

                        OutlinedTextField(
                            value = password.value,
                            onValueChange = { password.value = it },
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
                                        contentDescription = null
                                    )
                                }
                            },
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedContainerColor = WhiteSoft,
                                unfocusedContainerColor = WhiteSoft,
                                focusedBorderColor = BlushBeet.copy(alpha = 0.9f),
                                unfocusedBorderColor = BlushBeet.copy(alpha = 0.22f),
                                cursorColor = MaterialTheme.colorScheme.primary
                            )
                        )

                        Text(
                            text = "Forgot password?",
                            modifier = Modifier.align(Alignment.End),
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.72f)
                        )

                        Spacer(modifier = Modifier.height(4.dp))

                        Button(
                            onClick = {
                                scope.launch {
                                    try {
                                        val request = LoginUserRequest(
                                            email = email.value,
                                            password = password.value
                                        )

                                        val response = repository.loginUser(request)
                                        message.value = "Login successful"
                                        onLoginSuccess(response.id)
                                    } catch (e: Exception) {
                                        message.value = "Error: ${e.message}"
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
                                text = "Log In",
                                style = MaterialTheme.typography.titleLarge
                            )
                        }

                        Text(
                            text = "Don’t have an account? Sign up",
                            modifier = Modifier
                                .align(Alignment.CenterHorizontally)
                                .clickable { onGoToRegisterClick() },
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.80f)
                        )

                        if (message.value.isNotEmpty()) {
                            Text(
                                text = message.value,
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onBackground
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                Box(
                    modifier = Modifier
                        .fillMaxSize()
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.berrily4),
                        contentDescription = "Berrily illustration",
                        modifier = Modifier
                            .size(620.dp)
                            .align(Alignment.BottomEnd)
                            .offset(x = (40).dp, y = (-60).dp),
                        contentScale = ContentScale.Fit
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))
            }
        }
    }
}