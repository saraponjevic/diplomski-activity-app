package com.example.activityapp.ui.screens.planner

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material3.*
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.activityapp.data.remote.dto.planner.PlannerSuggestionDto
import com.example.activityapp.data.remote.dto.planner.PlannerTaskCreateRequestDto
import com.example.activityapp.data.remote.dto.planner.PlannerTaskResponseDto
import com.example.activityapp.data.remote.dto.planner.PlannerTaskUpdateRequestDto
import com.example.activityapp.data.repository.ActivityRepository
import com.example.activityapp.data.repository.PlannerRepository
import com.example.activityapp.ui.theme.AppBackground
import com.example.activityapp.ui.theme.AvocadoSmoothie
import com.example.activityapp.ui.theme.BlushBeet
import com.example.activityapp.ui.theme.CardBackground
import com.example.activityapp.ui.theme.PeachProtein
import com.example.activityapp.ui.theme.SavorySage
import com.example.activityapp.ui.theme.TextPrimary
import com.example.activityapp.ui.theme.TextSecondary
import com.example.activityapp.ui.theme.WhiteSoft
import kotlinx.coroutines.launch
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.format.TextStyle
import java.util.Locale
import androidx.compose.foundation.Image
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import com.example.activityapp.R



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
fun PlannerScreen(userId: Long, onBack: () -> Unit) {
    val repository = remember { PlannerRepository() }
    val scope = rememberCoroutineScope()
    val activityRepository = remember { ActivityRepository() }

    var tasks by remember { mutableStateOf<List<PlannerTaskResponseDto>>(emptyList()) }
    var errorMessage by remember { mutableStateOf("") }

    var showAddScreen by remember { mutableStateOf(false) }
    var taskToEdit by remember { mutableStateOf<PlannerTaskResponseDto?>(null) }

    var plannerSuggestions by remember { mutableStateOf<List<PlannerSuggestionDto>>(emptyList()) }
    var selectedSuggestion by remember { mutableStateOf<PlannerSuggestionDto?>(null) }

    var addScreenTitle by remember { mutableStateOf("Add Task") }
    var addScreenButtonText by remember { mutableStateOf("Save") }
    var addInitialTitle by remember { mutableStateOf("") }
    var addInitialDescription by remember { mutableStateOf("") }
    var addInitialHour by remember { mutableStateOf(11) }
    var addInitialMinute by remember { mutableStateOf(0) }
    var addInitialTaskType by remember { mutableStateOf("WORK") }
    var isEditMode by remember { mutableStateOf(false) }

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

    if (showAddScreen) {
        AddTaskBottomSheet(
            onDismiss = {
                showAddScreen = false
                selectedSuggestion = null
                taskToEdit = null
                isEditMode = false
            },
            screenTitle = addScreenTitle,
            saveButtonText = addScreenButtonText,
            initialTitle = addInitialTitle,
            initialDescription = addInitialDescription,
            initialHour = addInitialHour,
            initialMinute = addInitialMinute,
            initialTaskType = addInitialTaskType,
            onSave = { title, description, hour, minute, taskType ->
                scope.launch {
                    try {
                        if (isEditMode && taskToEdit != null) {
                            repository.updateTask(
                                taskToEdit!!.id,
                                PlannerTaskUpdateRequestDto(
                                    title = title,
                                    description = description.ifBlank { null },
                                    date = taskToEdit!!.date,
                                    time = String.format(
                                        Locale.getDefault(),
                                        "%02d:%02d",
                                        hour,
                                        minute
                                    ),
                                    taskType = taskType,
                                    completed = taskToEdit!!.completed
                                )
                            )
                        } else {
                            repository.addTask(
                                userId,
                                PlannerTaskCreateRequestDto(
                                    title = title,
                                    description = description.ifBlank { null },
                                    date = LocalDate.now().toString(),
                                    time = String.format(
                                        Locale.getDefault(),
                                        "%02d:%02d",
                                        hour,
                                        minute
                                    ),
                                    taskType = taskType,
                                    source = if (selectedSuggestion != null) "AI" else "MANUAL"
                                )
                            )
                        }

                        showAddScreen = false
                        selectedSuggestion = null
                        taskToEdit = null
                        isEditMode = false
                        loadTasks()
                    } catch (e: Exception) {
                        errorMessage = if (isEditMode) {
                            e.message ?: "Failed to update task"
                        } else {
                            e.message ?: "Failed to save task"
                        }
                    }
                }
            }
        )
        return
    }

    val today = LocalDate.now()
    val startOfWeek = today.with(DayOfWeek.MONDAY)
    val weekDays = (0..6).map { startOfWeek.plusDays(it.toLong()) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(AppBackground)
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp, vertical = 14.dp),
            verticalArrangement = Arrangement.spacedBy(18.dp)
        ) {
            item {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(
                        onClick = onBack,
                        modifier = Modifier.size(40.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.ArrowBackIosNew,
                            contentDescription = "Back",
                            tint = TextPrimary
                        )
                    }

                    Spacer(modifier = Modifier.width(8.dp))

                    Text(
                        text = "Daily plan",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.SemiBold,
                        color = TextPrimary
                    )
                }
            }

            item {
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    items(weekDays) { date ->
                        val isToday = date == today

                        Card(
                            shape = RoundedCornerShape(18.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = if (isToday) AvocadoSmoothie else WhiteSoft
                            ),
                            border = if (isToday) null else BorderStroke(1.dp, BlushBeet.copy(alpha = 0.35f))
                        ) {
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                modifier = Modifier
                                    .width(56.dp)
                                    .padding(vertical = 10.dp)
                            ) {
                                Text(
                                    text = date.dayOfMonth.toString(),
                                    style = MaterialTheme.typography.titleMedium,
                                    color = if (isToday) WhiteSoft else TextPrimary,
                                    fontWeight = FontWeight.SemiBold
                                )

                                Spacer(modifier = Modifier.height(2.dp))

                                Text(
                                    text = date.dayOfWeek.getDisplayName(TextStyle.SHORT, Locale.ENGLISH),
                                    style = MaterialTheme.typography.bodySmall,
                                    color = if (isToday) WhiteSoft.copy(alpha = 0.92f) else TextSecondary
                                )
                            }
                        }
                    }
                }
            }

            if (plannerSuggestions.isNotEmpty()) {
                item {
                    Column {
                        Text(
                            text = "Suggestions for today",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.SemiBold,
                            color = TextPrimary
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        Text(
                            text = "These are a few useful things you could add to today’s plan.",
                            style = MaterialTheme.typography.bodyMedium,
                            color = TextSecondary
                        )

                        Spacer(modifier = Modifier.height(14.dp))

                        LazyRow(
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            items(plannerSuggestions) { suggestion ->
                                PlannerSuggestionCard(
                                    suggestion = suggestion,
                                    tasks = tasks,
                                    onAddClick = {
                                        selectedSuggestion = suggestion
                                        taskToEdit = null
                                        isEditMode = false
                                        addScreenTitle = "Add suggestion to plan"
                                        addScreenButtonText = "Save"
                                        addInitialTitle = suggestion.title
                                        addInitialDescription = suggestion.description
                                        addInitialTaskType = suggestion.taskType

                                        val suggestedTime = getSuggestedDefaultTime(suggestion.recommendedPartOfDay)
                                        addInitialHour = suggestedTime.substringBefore(":").toIntOrNull() ?: 18
                                        addInitialMinute = suggestedTime.substringAfter(":").toIntOrNull() ?: 0

                                        showAddScreen = true
                                    }
                                )
                            }
                        }
                    }
                }
            }

            if (errorMessage.isNotEmpty()) {
                item {
                    Card(
                        colors = CardDefaults.cardColors(
                            containerColor = PeachProtein.copy(alpha = 0.65f)
                        ),
                        shape = RoundedCornerShape(18.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = "Error: $errorMessage",
                            color = TextPrimary,
                            style = MaterialTheme.typography.bodyMedium,
                            modifier = Modifier.padding(14.dp)
                        )
                    }
                }
            }

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
                        selectedSuggestion = null
                        isEditMode = true
                        addScreenTitle = "Edit Task"
                        addScreenButtonText = "Update"
                        addInitialTitle = task.title
                        addInitialDescription = task.description ?: ""
                        addInitialTaskType = task.taskType
                        addInitialHour = task.time.substringBefore(":").toIntOrNull() ?: 18
                        addInitialMinute = task.time.substringAfter(":").toIntOrNull() ?: 0
                        showAddScreen = true
                    }
                )
            }

            item {
                Spacer(modifier = Modifier.height(90.dp))
            }
        }

        FloatingActionButton(
            onClick = {
                isEditMode = false
                selectedSuggestion = null
                taskToEdit = null
                addScreenTitle = "Add Task"
                addScreenButtonText = "Save"
                addInitialTitle = ""
                addInitialDescription = ""
                addInitialHour = 11
                addInitialMinute = 0
                addInitialTaskType = "WORK"
                showAddScreen = true
            },
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(end = 20.dp, bottom = 24.dp),
            containerColor = BlushBeet,
            contentColor = WhiteSoft,
            shape = CircleShape
        ) {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = "Add task"
            )
        }
    }
}

@Composable
fun PlannerSuggestionCard(
    suggestion: PlannerSuggestionDto,
    tasks: List<PlannerTaskResponseDto>,
    onAddClick: () -> Unit
) {

    val alreadyAdded = tasks.any {
        it.title.equals(suggestion.title, ignoreCase = true)
    }

    Card(
        modifier = Modifier
            .width(270.dp)
            .height(250.dp),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(
            containerColor = CardBackground
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Text(
                text = suggestion.suggestionType.replace("_", " "),
                style = MaterialTheme.typography.labelLarge,
                color = SavorySage,
                fontWeight = FontWeight.SemiBold
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = suggestion.title,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
                color = TextPrimary
            )

            Spacer(modifier = Modifier.height(8.dp))

            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState())
            ) {
                Text(
                    text = suggestion.description,
                    style = MaterialTheme.typography.bodyMedium,
                    color = TextSecondary
                )

                Spacer(modifier = Modifier.height(10.dp))

                Text(
                    text = "${suggestion.suggestedDurationMinutes} min • ${suggestion.recommendedPartOfDay}",
                    style = MaterialTheme.typography.bodySmall,
                    color = TextSecondary
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            if (!alreadyAdded) {
                Button(
                    onClick = onAddClick,
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = AvocadoSmoothie,
                        contentColor = WhiteSoft
                    )
                ) {
                    Text("Add to plan")
                }
            } else {
                Button(
                    onClick = {},
                    enabled = false,
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = CardBackground,
                        contentColor = TextSecondary
                    )
                ) {
                    Text("Added")
                }
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
            label = task.time,
            backgroundColor = badgeColor
        )

        Spacer(modifier = Modifier.width(12.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = cardColor),
            elevation = CardDefaults.cardElevation(defaultElevation = 5.dp)
        ) {
            Box(
                modifier = Modifier.fillMaxWidth()
            ) {
                Image(
                    painter = painterResource(id = getTaskImage(task.taskType)),
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxSize()
                        .alpha(0.12f),
                    contentScale = ContentScale.Crop
                )

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Text(
                        text = task.taskType.replace("_", " "),
                        style = MaterialTheme.typography.labelLarge,
                        color = SavorySage,
                        fontWeight = FontWeight.SemiBold
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = task.title,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold,
                        color = TextPrimary
                    )

                    if (!task.description.isNullOrBlank()) {
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = task.description,
                            style = MaterialTheme.typography.bodyMedium,
                            color = TextSecondary
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))
                    HorizontalDivider(color = WhiteSoft.copy(alpha = 0.7f))
                    Spacer(modifier = Modifier.height(8.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Checkbox(
                                checked = task.completed,
                                onCheckedChange = onCheckedChange,
                                colors = CheckboxDefaults.colors(
                                    checkedColor = AvocadoSmoothie,
                                    uncheckedColor = SavorySage
                                )
                            )

                            Text(
                                text = if (task.completed) "Completed" else "Mark done",
                                style = MaterialTheme.typography.bodySmall,
                                color = TextSecondary
                            )
                        }

                        Row {
                            IconButton(onClick = onEditClick) {
                                Icon(
                                    imageVector = Icons.Default.Edit,
                                    contentDescription = "Edit task",
                                    tint = SavorySage
                                )
                            }

                            IconButton(onClick = onDeleteClick) {
                                Icon(
                                    imageVector = Icons.Default.Delete,
                                    contentDescription = "Delete task",
                                    tint = BlushBeet
                                )
                            }
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
        shadowElevation = 4.dp,
        modifier = Modifier.width(38.dp)
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .height(120.dp)
                .padding(vertical = 10.dp)
        ) {
            Text(
                text = label.substring(0,5),
                modifier = Modifier.rotate(-90f),
                color = WhiteSoft,
                style = MaterialTheme.typography.bodySmall,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

fun getTaskCardColor(taskType: String): Color {
    return when (taskType.uppercase()) {
        "MEETING" -> PeachProtein.copy(alpha = 0.9f)
        "STUDY" -> BlushBeet.copy(alpha = 0.45f)
        "TRAVEL", "TRAVELING" -> Color(0xFFE8DFC1)
        "WORK" -> Color(0xFFDDE7D2)
        "EXERCISE", "WALK" -> Color(0xFFD7E6C3)
        "REST" -> CardBackground
        else -> WhiteSoft
    }
}

fun getTaskBadgeColor(taskType: String): Color {
    return when (taskType.uppercase()) {
        "MEETING" -> BlushBeet
        "STUDY" -> Color(0xFFC5A7BC)
        "TRAVEL", "TRAVELING" -> Color(0xFFC8B27A)
        "WORK" -> SavorySage
        "EXERCISE", "WALK" -> AvocadoSmoothie
        "REST" -> Color(0xFFB8B5A6)
        else -> SavorySage
    }
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

fun getTaskImage(taskType: String): Int {
    return when (taskType.uppercase()) {
        "WORK" -> R.drawable.flowers_transparent
        "EXERCISE", "WALK" -> R.drawable.flowers_transparent
        "STUDY" -> R.drawable.flowers_transparent
        "TRAVEL" -> R.drawable.flowers_transparent
        "MEETING" -> R.drawable.flowers_transparent
        "REST" -> R.drawable.flowers_transparent
        else -> R.drawable.flowers_transparent
    }
}