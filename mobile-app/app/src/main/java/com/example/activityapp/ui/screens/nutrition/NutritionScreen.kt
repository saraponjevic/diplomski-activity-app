package com.example.activityapp.ui.screens.nutrition

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.activityapp.R
import com.example.activityapp.data.remote.dto.nutrition.MealSuggestionResponse
import com.example.activityapp.ui.theme.AvocadoSmoothie
import com.example.activityapp.ui.theme.BlushBeet
import com.example.activityapp.ui.theme.OatLatte
import com.example.activityapp.ui.theme.TextPrimary
import com.example.activityapp.ui.theme.TextSecondary
import com.example.activityapp.ui.theme.WhiteSoft
import com.example.activityapp.ui.viewmodel.DashboardViewModel

@Composable
fun NutritionScreen(
    userId: Long,
    dashboardViewModel: DashboardViewModel = viewModel(),
    onBack: () -> Unit
) {
    val recommendation by dashboardViewModel.latestRecommendation.collectAsState()

    LaunchedEffect(userId) {
        dashboardViewModel.loadDashboardData(userId)
    }

    val nutrition = recommendation?.nutrition
    val meals = nutrition?.meals ?: emptyList()

    var selectedMealType by remember { mutableStateOf("BREAKFAST") }
    var selectedMeal by remember { mutableStateOf<MealSuggestionResponse?>(null) }

    if (selectedMeal != null) {
        MealDetailScreen(
            meal = selectedMeal!!,
            onBackClick = { selectedMeal = null }
        )
        return
    }

    val selectedMeals = getMealsByType(meals, selectedMealType)

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        LazyColumn(


            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 20.dp, vertical = 20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            item {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = null,
                            tint = TextPrimary
                        )
                    }

                    Text(
                        "Nutrition plan for you",
                        style = MaterialTheme.typography.titleMedium,
                        color = TextPrimary
                    )
                }
            }

            item {
                NutritionHeroSection(
                    nutritionStatus = nutrition?.nutritionStatus ?: "No data"
                )
            }

            item {
                NutritionSecondaryCardsRow(
                    waterTip = nutrition?.waterIntakeTip ?: "No data",
                    nutritionTip = nutrition?.nutritionTip ?: "No data"
                )
            }

            item {
                MealTypeTabs(
                    selectedMealType = selectedMealType,
                    onMealTypeSelected = { selectedMealType = it }
                )
            }

            item {
                Text(
                    text = formatMealType(selectedMealType),
                    style = MaterialTheme.typography.titleLarge,
                    color = TextPrimary
                )
            }

            item {
                if (selectedMeals.isEmpty()) {
                    Text(
                        text = "No suggestions available",
                        style = MaterialTheme.typography.bodyMedium,
                        color = TextSecondary
                    )
                } else {
                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(16.dp),
                        contentPadding = PaddingValues(horizontal = 4.dp)
                    ) {
                        items(selectedMeals) { meal ->
                            MealSuggestionCard(
                                meal = meal,
                                onClick = { selectedMeal = meal }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun NutritionHeroSection(
    nutritionStatus: String
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Card(
            modifier = Modifier.size(92.dp),
            shape = CircleShape,
            colors = CardDefaults.cardColors(
                containerColor = BlushBeet.copy(alpha = 0.22f)
            ),
            elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(8.dp),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(id = R.drawable.jagodaa),
                    contentDescription = "Nutrition illustration",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(CircleShape)
                )
            }
        }

        Box(
            modifier = Modifier.weight(1f)
        ) {

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 8.dp),
                shape = RoundedCornerShape(28.dp),
                colors = CardDefaults.cardColors(
                    containerColor = WhiteSoft
                ),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 18.dp, vertical = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {


                    Text(
                        text = getNutritionStatusMessage(nutritionStatus),
                        style = MaterialTheme.typography.bodyMedium,
                        color = TextSecondary
                    )
                }
            }

            Card(
                modifier = Modifier
                    .size(20.dp)
                    .align(Alignment.CenterStart)
                    .offset(x = (-6).dp, y = 20.dp),
                shape = CircleShape,
                colors = CardDefaults.cardColors(
                    containerColor = WhiteSoft
                ),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {}

            Card(
                modifier = Modifier
                    .size(12.dp)
                    .align(Alignment.CenterStart)
                    .offset(x = (-12).dp, y = 36.dp),
                shape = CircleShape,
                colors = CardDefaults.cardColors(
                    containerColor = WhiteSoft
                ),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {}
        }
    }
}

@Composable
fun NutritionSecondaryCardsRow(
    waterTip: String,
    nutritionTip: String
) {
    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(14.dp),
        contentPadding = PaddingValues(horizontal = 4.dp)
    ) {
        item {
            NutritionHighlightCard(
                title = "Water intake",
                text = waterTip
            )
        }

        item {
            NutritionHighlightCard(
                title = "Nutrition tip",
                text = nutritionTip
            )
        }
    }
}

@Composable
fun NutritionHighlightCard(
    title: String,
    text: String
) {
    val backgroundImage =
        if (title == "Water intake") R.drawable.jagoda_voda
        else R.drawable.jagoda

    val backgroundColor =
        if (title == "Water intake") AvocadoSmoothie
        else OatLatte

    Card(
        modifier = Modifier
            .width(220.dp)
            .height(150.dp),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(
            containerColor = backgroundColor.copy(alpha = 0.6f)
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Box {

            Image(
                painter = painterResource(id = backgroundImage),
                contentDescription = null,
                modifier = Modifier
                    .size(120.dp)
                    .align(Alignment.BottomEnd)
                    .offset(x = 20.dp, y = 20.dp),
                contentScale = ContentScale.Fit,
                alpha = 0.25f
            )

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(18.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {

                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium,
                    color = TextPrimary
                )

                Text(
                    text = text,
                    style = MaterialTheme.typography.bodyMedium,
                    color = TextSecondary
                )
            }
        }
    }
}

@Composable
fun MealTypeTabs(
    selectedMealType: String,
    onMealTypeSelected: (String) -> Unit
) {
    val mealTypes = listOf("BREAKFAST", "LUNCH", "DINNER", "SNACK")

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .horizontalScroll(rememberScrollState()),
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        mealTypes.forEach { mealType ->
            val isSelected = mealType == selectedMealType

            Surface(
                modifier = Modifier.clickable { onMealTypeSelected(mealType) },
                shape = RoundedCornerShape(50),
                color = if (isSelected) BlushBeet.copy(alpha = 0.85f) else WhiteSoft,
                tonalElevation = if (isSelected) 3.dp else 0.dp,
                shadowElevation = if (isSelected) 4.dp else 0.dp
            ) {
                Box(
                    modifier = Modifier.padding(horizontal = 18.dp, vertical = 10.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = formatMealType(mealType),
                        style = MaterialTheme.typography.bodyMedium,
                        color = if (isSelected) TextPrimary else TextSecondary
                    )
                }
            }
        }
    }
}

@Composable
fun MealSuggestionCard(
    meal: MealSuggestionResponse,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .width(220.dp)
            .padding(top = 74.dp, bottom = 12.dp)
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(350.dp),
            shape = RoundedCornerShape(30.dp),
            colors = CardDefaults.cardColors(
                containerColor = AvocadoSmoothie.copy(alpha = 0.25f)
            ),
            elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(start = 18.dp, end = 18.dp, top = 112.dp, bottom = 18.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Text(
                    text = meal.title ?: "No title",
                    style = MaterialTheme.typography.titleLarge,
                    color = TextPrimary
                )

                Text(
                    text = meal.description ?: "No description",
                    style = MaterialTheme.typography.bodyMedium,
                    color = TextSecondary
                )

                Text(
                    text = "Calories: ${meal.calories ?: 0} kcal",
                    style = MaterialTheme.typography.bodyMedium,
                    color = TextPrimary
                )

                Spacer(modifier = Modifier.weight(1f))

                Surface(
                    modifier = Modifier.clickable { onClick() },
                    shape = RoundedCornerShape(22.dp),
                    color = BlushBeet.copy(alpha = 0.9f),
                    tonalElevation = 0.dp,
                    shadowElevation = 3.dp
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 12.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "RECIPE",
                            style = MaterialTheme.typography.labelLarge,
                            color = TextPrimary
                        )
                    }
                }
            }
        }

        Card(
            modifier = Modifier
                .size(138.dp)
                .align(Alignment.TopCenter)
                .offset(x = 34.dp, y = (-62).dp),
            shape = CircleShape,
            colors = CardDefaults.cardColors(
                containerColor = WhiteSoft
            ),
            elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(6.dp)
            ) {
                Image(
                    painter = painterResource(id = getMealImageRes(meal.imageKey)),
                    contentDescription = meal.title ?: "Meal image",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(CircleShape)
                )
            }
        }
    }
}

@Composable
fun MealDetailScreen(
    meal: MealSuggestionResponse,
    onBackClick: () -> Unit
) {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 20.dp, vertical = 20.dp)
            ) {
                IconButton(onClick = onBackClick) {
                    Icon(
                        imageVector = Icons.Default.ArrowBackIosNew,
                        contentDescription = "Back",
                        tint = TextPrimary
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                Text(
                    text = meal.title ?: "Meal",
                    style = MaterialTheme.typography.headlineMedium,
                    color = TextPrimary
                )

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = "• ${meal.calories ?: 0} kcal",
                    style = MaterialTheme.typography.titleMedium,
                    color = TextSecondary
                )

                Spacer(modifier = Modifier.height(6.dp))

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(300.dp)
                ) {
                    Card(
                        modifier = Modifier
                            .size(320.dp)
                            .align(Alignment.TopEnd)
                            .offset(x = 110.dp, y = 0.dp),
                        shape = CircleShape,
                        colors = CardDefaults.cardColors(
                            containerColor = AvocadoSmoothie.copy(alpha = 0.28f)
                        ),
                        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(8.dp)
                        ) {
                            Image(
                                painter = painterResource(id = getMealImageRes(meal.imageKey)),
                                contentDescription = meal.title ?: "Meal image",
                                contentScale = ContentScale.Crop,
                                modifier = Modifier
                                    .fillMaxSize()
                                    .clip(CircleShape)
                            )
                        }
                    }
                }

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .offset(y = (-86).dp)
                ) {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(34.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = OatLatte
                        ),
                        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
                    ) {
                        Column(
                            modifier = Modifier.padding(horizontal = 22.dp, vertical = 22.dp),
                            verticalArrangement = Arrangement.spacedBy(14.dp)
                        ) {
                            Box(
                                modifier = Modifier.fillMaxWidth(),
                                contentAlignment = Alignment.Center
                            ) {
                                Box(
                                    modifier = Modifier
                                        .width(52.dp)
                                        .height(5.dp)
                                        .clip(RoundedCornerShape(50))
                                ) {
                                    Surface(
                                        modifier = Modifier.fillMaxSize(),
                                        color = BlushBeet.copy(alpha = 0.5f)
                                    ) {}
                                }
                            }

                            Text(
                                text = "Recipe",
                                style = MaterialTheme.typography.titleLarge,
                                color = TextPrimary
                            )

                            (meal.recipe ?: emptyList()).forEachIndexed { index, step ->
                                Text(
                                    text = "${index + 1}. $step",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = TextSecondary
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

fun formatMealType(mealType: String): String {
    return when (mealType) {
        "BREAKFAST" -> "Breakfast"
        "LUNCH" -> "Lunch"
        "DINNER" -> "Dinner"
        "SNACK" -> "Snack"
        else -> mealType
    }
}

fun getMealsByType(
    meals: List<MealSuggestionResponse>,
    mealType: String
): List<MealSuggestionResponse> {
    return meals
        .filter { it.mealType == mealType }
        .sortedBy { it.sortOrder ?: Int.MAX_VALUE }
}

fun getNutritionStatusMessage(status: String?): String {
    return when (status) {
        "LIGHT_SUPPORT" -> "Today is a great day for lighter meals, good hydration and steady energy."
        "BALANCED_ENERGY" -> "You are doing well today — balanced meals will help you keep that rhythm."
        "WORKOUT_SUPPORT" -> "You have a strong active vibe today, so give your body quality fuel and protein."
        "RECOVERY_FOCUS" -> "Take it easy today and focus on recovery, hydration and gentle meals."
        else -> "Listen to your body today and choose meals that feel light, balanced and nourishing."
    }
}