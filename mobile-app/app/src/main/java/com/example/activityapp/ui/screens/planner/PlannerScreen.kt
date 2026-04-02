package com.example.activityapp.ui.screens.planner

import android.app.TimePickerDialog
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.activityapp.data.remote.dto.planner.PlannerSuggestionDto
import com.example.activityapp.data.remote.dto.planner.PlannerTaskCreateRequestDto
import com.example.activityapp.data.remote.dto.planner.PlannerTaskResponseDto
import com.example.activityapp.data.remote.dto.planner.PlannerTaskUpdateRequestDto
import com.example.activityapp.data.repository.ActivityRepository
import com.example.activityapp.data.repository.PlannerRepository
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.util.Locale

private val plannerTaskTypes = listOf(
    "WALK",
    "EXERCISE",
    "STUDY",
    "WORK",
    "TRAVEL",
    "MEETING",
    "REST"
)

@Composable
fun PlannerScreen(userId: Long) {
    val repository = remember { PlannerRepository() }
    val scope = rememberCoroutineScope()

    var tasks by remember { mutableStateOf<List<PlannerTaskResponseDto>>(emptyList()) }
    var errorMessage by remember { mutableStateOf("") }
    var showAddDialog by remember { mutableStateOf(false) }
    var taskToEdit by remember { mutableStateOf<PlannerTaskResponseDto?>(null) }

    val activityRepository = remember { ActivityRepository() }

    var plannerSuggestions by remember { mutableStateOf<List<PlannerSuggestionDto>>(emptyList()) }
    var selectedSuggestion by remember { mutableStateOf<PlannerSuggestionDto?>(null) }

    fun loadTasks() {
        scope.launch {
            try {
                tasks = repository.getTodayTasks(userId)
                errorMessage = ""
            } catch (e: Exception) {
                errorMessage = e.message ?: "Unknown error"
            }
        }
    }

    fun loadPlannerSuggestions() {
        scope.launch {
            try {
                val latestRecommendation = activityRepository.getLatestRecommendation(userId)
                plannerSuggestions = latestRecommendation.plannerSuggestions
            } catch (e: Exception) {
                plannerSuggestions = emptyList()
            }
        }
    }


    LaunchedEffect(userId) {
        loadTasks()
        loadPlannerSuggestions()
    }



    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFFFF7F8))
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp, vertical = 14.dp)
        ) {
            Text(
                text = "Daily Task",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF2F2A2A)
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = "Today • ${LocalDate.now()}",
                style = MaterialTheme.typography.bodyMedium,
                color = Color(0xFF8C7E7E)
            )

            Spacer(modifier = Modifier.height(18.dp))

            if (plannerSuggestions.isNotEmpty()) {
                Text(
                    text = "Smart suggestions for today",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF3F3535)
                )

                Spacer(modifier = Modifier.height(10.dp))

                Text(
                    text = "These are a few useful things you could add to today’s plan.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color(0xFF8C7E7E)
                )

                Spacer(modifier = Modifier.height(14.dp))

                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(plannerSuggestions) { suggestion ->
                        PlannerSuggestionCard(
                            suggestion = suggestion,
                            onAddClick = {
                                selectedSuggestion = suggestion
                            }
                        )
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))
            }

            if (errorMessage.isNotEmpty()) {
                Card(
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFFFE2E2)),
                    shape = RoundedCornerShape(16.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "Error: $errorMessage",
                        color = Color(0xFF9B2C2C),
                        modifier = Modifier.padding(12.dp)
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))
            }

            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(18.dp)
            ) {
                items(tasks) { task ->
                    PlannerTaskStyledCard(
                        task = task,
                        onCheckedChange = { checked ->
                            scope.launch {
                                repository.markCompleted(task.id, checked)
                                loadTasks()
                            }
                        },
                        onDeleteClick = {
                            scope.launch {
                                repository.deleteTask(task.id)
                                loadTasks()
                            }
                        },
                        onEditClick = {
                            taskToEdit = task
                        }
                    )
                }
            }
        }

        FloatingActionButton(
            onClick = { showAddDialog = true },
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 22.dp),
            containerColor = Color(0xFFE78A8A),
            contentColor = Color.White,
            shape = CircleShape
        ) {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = "Add task"
            )
        }
    }

    if (showAddDialog) {
        PlannerTaskDialog(
            titleText = "Create New Task",
            initialTitle = "",
            initialDescription = "",
            initialDate = LocalDate.now().toString(),
            initialTime = "18:00",
            initialTaskType = "MEETING",
            confirmText = "Save",
            onDismiss = { showAddDialog = false },
            onConfirm = { title, description, date, time, taskType ->
                scope.launch {
                    try {
                        repository.addTask(
                            userId,
                            PlannerTaskCreateRequestDto(
                                title = title,
                                description = description.ifBlank { null },
                                date = date,
                                time = time,
                                taskType = taskType,
                                source = "MANUAL"
                            )
                        )
                        showAddDialog = false
                        loadTasks()
                    } catch (e: Exception) {
                        errorMessage = e.message ?: "Failed to add task"
                    }
                }
            }
        )
    }

    selectedSuggestion?.let { suggestion ->
        PlannerTaskDialog(
            titleText = "Add suggestion to plan",
            initialTitle = suggestion.title,
            initialDescription = suggestion.description,
            initialDate = LocalDate.now().toString(),
            initialTime = getSuggestedDefaultTime(suggestion.recommendedPartOfDay),
            initialTaskType = suggestion.taskType,
            confirmText = "Add",
            onDismiss = { selectedSuggestion = null },
            onConfirm = { title, description, date, time, taskType ->
                scope.launch {
                    try {
                        repository.addTask(
                            userId,
                            PlannerTaskCreateRequestDto(
                                title = title,
                                description = description.ifBlank { null },
                                date = date,
                                time = normalizeTimeForRequest(time),
                                taskType = taskType,
                                source = "AI"
                            )
                        )
                        selectedSuggestion = null
                        loadTasks()
                    } catch (e: Exception) {
                        errorMessage = e.message ?: "Failed to add AI suggestion"
                    }
                }
            }
        )
    }

    taskToEdit?.let { task ->
        PlannerTaskDialog(
            titleText = "Edit Task",
            initialTitle = task.title,
            initialDescription = task.description ?: "",
            initialDate = task.date,
            initialTime = normalizeTimeForRequest(task.time),
            initialTaskType = task.taskType,
            confirmText = "Update",
            onDismiss = { taskToEdit = null },
            onConfirm = { title, description, date, time, taskType ->
                scope.launch {
                    try {
                        repository.updateTask(
                            task.id,
                            PlannerTaskUpdateRequestDto(
                                title = title,
                                description = description.ifBlank { null },
                                date = date,
                                time = normalizeTimeForRequest(time),
                                taskType = taskType,
                                completed = task.completed
                            )
                        )
                        taskToEdit = null
                        loadTasks()
                    } catch (e: Exception) {
                        errorMessage = e.message ?: "Failed to update task"
                    }
                }
            }
        )
    }
}

@Composable
fun PlannerSuggestionCard(
    suggestion: PlannerSuggestionDto,
    onAddClick: () -> Unit
) {
    Card(
        modifier = Modifier.width(260.dp),
        shape = RoundedCornerShape(22.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFEFE6FF)),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(
                text = suggestion.suggestionType.replace("_", " "),
                style = MaterialTheme.typography.labelMedium,
                color = Color(0xFF7B61A8),
                fontWeight = FontWeight.SemiBold
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = suggestion.title,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF3D2F4F)
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = suggestion.description,
                style = MaterialTheme.typography.bodyMedium,
                color = Color(0xFF5D516D)
            )

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = "${suggestion.suggestedDurationMinutes} min • ${suggestion.recommendedPartOfDay}",
                style = MaterialTheme.typography.bodySmall,
                color = Color(0xFF7E738D)
            )

            Spacer(modifier = Modifier.height(14.dp))

            Button(
                onClick = onAddClick,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Add to plan")
            }
        }
    }
}

@Composable
fun PlannerTaskStyledCard(
    task: PlannerTaskResponseDto,
    onCheckedChange: (Boolean) -> Unit,
    onDeleteClick: () -> Unit,
    onEditClick: () -> Unit
) {
    val cardColor = getTaskCardColor(task.taskType)
    val badgeColor = getTaskBadgeColor(task.taskType)

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth()
    ) {
        TaskTypeBadge(
            label = task.taskType.replace("_", " "),
            backgroundColor = badgeColor
        )

        Spacer(modifier = Modifier.width(12.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(22.dp),
            colors = CardDefaults.cardColors(containerColor = cardColor),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Schedule,
                        contentDescription = null,
                        tint = Color(0xFF7A5C5C),
                        modifier = Modifier.size(16.dp)
                    )

                    Spacer(modifier = Modifier.width(6.dp))

                    Text(
                        text = task.time,
                        style = MaterialTheme.typography.bodySmall,
                        color = Color(0xFF7A5C5C),
                        fontWeight = FontWeight.Medium
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = task.title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF4A3D3D)
                )

                if (!task.description.isNullOrBlank()) {
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = task.description,
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color(0xFF6E5B5B)
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))
                HorizontalDivider(color = Color.White.copy(alpha = 0.6f))
                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Checkbox(
                            checked = task.completed,
                            onCheckedChange = onCheckedChange
                        )

                        Text(
                            text = if (task.completed) "Completed" else "Mark done",
                            style = MaterialTheme.typography.bodySmall,
                            color = Color(0xFF6E5B5B)
                        )
                    }

                    Row {
                        IconButton(onClick = onEditClick) {
                            Icon(
                                imageVector = Icons.Default.Edit,
                                contentDescription = "Edit task",
                                tint = Color(0xFF8F5C5C)
                            )
                        }

                        IconButton(onClick = onDeleteClick) {
                            Icon(
                                imageVector = Icons.Default.Delete,
                                contentDescription = "Delete task",
                                tint = Color(0xFF8F5C5C)
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun TaskTypeBadge(
    label: String,
    backgroundColor: Color
) {
    Surface(
        shape = RoundedCornerShape(50),
        color = backgroundColor,
        shadowElevation = 6.dp,
        modifier = Modifier.width(38.dp)
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .height(120.dp)
                .padding(vertical = 10.dp)
        ) {
            Text(
                text = label,
                modifier = Modifier.rotate(-90f),
                color = Color.White,
                style = MaterialTheme.typography.bodySmall,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

fun getTaskCardColor(taskType: String): Color {
    return when (taskType.uppercase()) {
        "MEETING" -> Color(0xFFFFD9DE)
        "STUDY" -> Color(0xFFE9C7FF)
        "TRAVEL", "TRAVELING" -> Color(0xFFFFE08A)
        "WORK" -> Color(0xFFCDEBFF)
        "EXERCISE" -> Color(0xFFD7F5D8)
        else -> Color(0xFFFFE7EC)
    }
}

fun getTaskBadgeColor(taskType: String): Color {
    return when (taskType.uppercase()) {
        "MEETING" -> Color(0xFFD98C98)
        "STUDY" -> Color(0xFFB26AE2)
        "TRAVEL", "TRAVELING" -> Color(0xFFD8A800)
        "WORK" -> Color(0xFF67A9D8)
        "EXERCISE" -> Color(0xFF5CBF84)
        else -> Color(0xFFE78A8A)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlannerTaskDialog(
    titleText: String,
    initialTitle: String,
    initialDescription: String,
    initialDate: String,
    initialTime: String,
    initialTaskType: String,
    confirmText: String,
    onDismiss: () -> Unit,
    onConfirm: (String, String, String, String, String) -> Unit
) {
    val context = LocalContext.current

    var title by remember { mutableStateOf(initialTitle) }
    var description by remember { mutableStateOf(initialDescription) }
    var date by remember { mutableStateOf(initialDate) }
    var time by remember { mutableStateOf(initialTime) }
    var taskType by remember { mutableStateOf(initialTaskType) }

    var expanded by remember { mutableStateOf(false) }

    var showTimePicker by remember { mutableStateOf(false) }

    val initialHour = initialTime.substringBefore(":").toIntOrNull() ?: 18
    val initialMinute = initialTime.substringAfter(":").toIntOrNull() ?: 0

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = titleText,
                fontWeight = FontWeight.Bold
            )
        },
        text = {
            Column(
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text("Title") },
                    singleLine = true
                )

                OutlinedTextField(
                    value = description,
                    onValueChange = { description = it },
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text("Description") }
                )



                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { showTimePicker = true }
                ) {
                    OutlinedTextField(
                        value = time,
                        onValueChange = {},
                        readOnly = true,
                        enabled = true,
                        modifier = Modifier.fillMaxWidth(),
                        label = { Text("Time") },
                        trailingIcon = {
                            IconButton(onClick = { showTimePicker = true }) {
                                Icon(
                                    imageVector = Icons.Default.Schedule,
                                    contentDescription = "Select time"
                                )
                            }
                        }
                    )
                }

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
                        label = { Text("Task Type") },
                        trailingIcon = {
                            ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
                        }
                    )

                    ExposedDropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false }
                    ) {
                        plannerTaskTypes.forEach { type ->
                            DropdownMenuItem(
                                text = { Text(type) },
                                onClick = {
                                    taskType = type
                                    expanded = false
                                }
                            )
                        }
                    }
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    onConfirm(
                        title.trim(),
                        description.trim(),
                        date.trim(),
                        time.trim(),
                        taskType.trim()
                    )
                },
                enabled = title.isNotBlank()
            ) {
                Text(confirmText)
            }
        },
        dismissButton = {
            OutlinedButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )

    if (showTimePicker) {
        TimePickerDialog(
            context,
            { _, selectedHour, selectedMinute ->
                time = String.format(
                    Locale.getDefault(),
                    "%02d:%02d",
                    selectedHour,
                    selectedMinute
                )
                showTimePicker = false
            },
            initialHour,
            initialMinute,
            true
        ).apply {
            setOnDismissListener {
                showTimePicker = false
            }
        }.show()
    }
}

private fun normalizeTimeForRequest(time: String): String {
    return if (time.length >= 5) time.substring(0, 5) else time
}

private fun getSuggestedDefaultTime(partOfDay: String): String {
    return when (partOfDay.uppercase()) {
        "MORNING" -> "09:00"
        "AFTERNOON" -> "14:00"
        "EVENING" -> "18:00"
        "NIGHT" -> "21:00"
        else -> "18:00"
    }
}

