package com.example.activityapp.ui.screens.planner

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.activityapp.ui.theme.AppBackground
import com.example.activityapp.ui.theme.AvocadoSmoothie
import com.example.activityapp.ui.theme.BlushBeet
import com.example.activityapp.ui.theme.CardBackground
import com.example.activityapp.ui.theme.TextPrimary
import com.example.activityapp.ui.theme.TextSecondary
import com.example.activityapp.ui.theme.WhiteSoft

private val plannerTaskTypes = listOf(
    "WALK",
    "EXERCISE",
    "STUDY",
    "WORK",
    "TRAVEL",
    "MEETING",
    "REST"
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddTaskBottomSheet(
    onDismiss: () -> Unit,
    onSave: (String, String, Int, Int, String) -> Unit,
    screenTitle: String = "Add Task",
    saveButtonText: String = "Save",
    initialTitle: String = "",
    initialDescription: String = "",
    initialHour: Int = 11,
    initialMinute: Int = 2,
    initialTaskType: String = "WORK"
) {
    var title by remember { mutableStateOf(initialTitle) }
    var description by remember { mutableStateOf(initialDescription) }
    var hour by remember { mutableIntStateOf(initialHour) }
    var minute by remember { mutableIntStateOf(initialMinute) }
    var taskType by remember { mutableStateOf(initialTaskType) }
    var expanded by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(AppBackground)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .background(AppBackground)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(170.dp)
                    .background(
                        color = AvocadoSmoothie.copy(alpha = 0.92f),
                        shape = RoundedCornerShape(
                            bottomStart = 34.dp,
                            bottomEnd = 34.dp
                        )
                    )
                    .padding(horizontal = 20.dp, vertical = 18.dp)
            ) {
                IconButton(
                    onClick = onDismiss,
                    modifier = Modifier
                        .align(Alignment.TopStart)
                        .background(
                            color = WhiteSoft.copy(alpha = 0.18f),
                            shape = CircleShape
                        )
                ) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Close",
                        tint = WhiteSoft
                    )
                }

                Text(
                    text = screenTitle,
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = WhiteSoft,
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .padding(start = 4.dp, bottom = 8.dp)
                )
            }

            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .offset(y = (-26).dp)
                    .padding(horizontal = 16.dp),
                shape = RoundedCornerShape(34.dp),
                color = WhiteSoft,
                shadowElevation = 8.dp
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 18.dp, vertical = 18.dp)
                ) {
                    Text(
                        text = "Time",
                        style = MaterialTheme.typography.titleMedium,
                        color = TextPrimary,
                        fontWeight = FontWeight.SemiBold
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        TimeWheelColumn(
                            label = "Hour",
                            value = hour,
                            range = 0..23,
                            onValueChange = { hour = it }
                        )

                        Spacer(modifier = Modifier.width(32.dp))

                        TimeWheelColumn(
                            label = "Minute",
                            value = minute,
                            range = 0..59,
                            onValueChange = { minute = it }
                        )
                    }

                    Spacer(modifier = Modifier.height(18.dp))

                    Text(
                        text = "Title",
                        style = MaterialTheme.typography.titleMedium,
                        color = TextPrimary,
                        fontWeight = FontWeight.SemiBold
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    OutlinedTextField(
                        value = title,
                        onValueChange = { title = it },
                        placeholder = {
                            Text(
                                "Write the title",
                                color = TextSecondary.copy(alpha = 0.7f)
                            )
                        },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(20.dp),
                        singleLine = true,
                        colors = plannerTextFieldColors()
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    Text(
                        text = "Note",
                        style = MaterialTheme.typography.titleMedium,
                        color = TextPrimary,
                        fontWeight = FontWeight.SemiBold
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    OutlinedTextField(
                        value = description,
                        onValueChange = { description = it },
                        placeholder = {
                            Text(
                                "Write your important note",
                                color = TextSecondary.copy(alpha = 0.7f)
                            )
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(110.dp),
                        shape = RoundedCornerShape(20.dp),
                        colors = plannerTextFieldColors()
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    Text(
                        text = "Task Type",
                        style = MaterialTheme.typography.titleMedium,
                        color = TextPrimary,
                        fontWeight = FontWeight.SemiBold
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    ExposedDropdownMenuBox(
                        expanded = expanded,
                        onExpandedChange = { expanded = !expanded }
                    ) {
                        OutlinedTextField(
                            value = taskType,
                            onValueChange = {},
                            readOnly = true,
                            modifier = Modifier
                                .fillMaxWidth()
                                .menuAnchor(),
                            shape = RoundedCornerShape(20.dp),
                            trailingIcon = {
                                ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
                            },
                            colors = plannerTextFieldColors()
                        )

                        ExposedDropdownMenu(
                            expanded = expanded,
                            onDismissRequest = { expanded = false }
                        ) {
                            plannerTaskTypes.forEach { type ->
                                DropdownMenuItem(
                                    text = {
                                        Text(
                                            text = type,
                                            style = MaterialTheme.typography.bodyLarge,
                                            color = TextPrimary
                                        )
                                    },
                                    onClick = {
                                        taskType = type
                                        expanded = false
                                    }
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(22.dp))

                    Button(
                        onClick = {
                            onSave(
                                title.trim(),
                                description.trim(),
                                hour,
                                minute,
                                taskType
                            )
                        },
                        enabled = title.isNotBlank(),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(52.dp),
                        shape = RoundedCornerShape(22.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = AvocadoSmoothie,
                            contentColor = WhiteSoft,
                            disabledContainerColor = CardBackground,
                            disabledContentColor = TextSecondary
                        )
                    ) {
                        Text(
                            text = saveButtonText,
                            style = MaterialTheme.typography.labelLarge,
                            fontWeight = FontWeight.SemiBold
                        )
                    }

                    Spacer(modifier = Modifier.height(6.dp))

                    TextButton(
                        onClick = onDismiss,
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    ) {
                        Text(
                            text = "Cancel",
                            color = TextSecondary,
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))
                }
            }
        }
    }
}

@Composable
private fun TimeWheelColumn(
    label: String,
    value: Int,
    range: IntRange,
    onValueChange: (Int) -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodySmall,
            color = TextSecondary
        )

        Spacer(modifier = Modifier.height(4.dp))

        TextButton(
            onClick = {
                val next = if (value < range.last) value + 1 else range.first
                onValueChange(next)
            }
        ) {
            Text(
                text = "+",
                style = MaterialTheme.typography.titleMedium,
                color = TextSecondary
            )
        }

        Text(
            text = value.toString().padStart(2, '0'),
            style = MaterialTheme.typography.headlineMedium,
            color = TextPrimary,
            fontWeight = FontWeight.SemiBold
        )

        TextButton(
            onClick = {
                val previous = if (value > range.first) value - 1 else range.last
                onValueChange(previous)
            }
        ) {
            Text(
                text = "-",
                style = MaterialTheme.typography.titleMedium,
                color = TextSecondary
            )
        }
    }
}

@Composable
private fun plannerTextFieldColors() = OutlinedTextFieldDefaults.colors(
    focusedBorderColor = AvocadoSmoothie,
    unfocusedBorderColor = BlushBeet.copy(alpha = 0.45f),
    focusedLabelColor = TextPrimary,
    unfocusedLabelColor = TextSecondary,
    focusedTextColor = TextPrimary,
    unfocusedTextColor = TextPrimary,
    cursorColor = TextPrimary,
    focusedContainerColor = Color(0xFFFDFBF8),
    unfocusedContainerColor = Color(0xFFFDFBF8)
)