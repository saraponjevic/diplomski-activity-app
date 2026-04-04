package com.example.activityapp.ui.screens.wellnesss

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.activityapp.R
import com.example.activityapp.ui.theme.*
import com.example.activityapp.ui.viewmodel.DashboardViewModel

@Composable
fun WellnessResultScreen(
    userId: Long,
    selectedMood: String,
    dashboardViewModel: DashboardViewModel,
    onBack: () -> Unit
) {
    val wellnessDetails by dashboardViewModel.wellnessDetails.collectAsState()

    LaunchedEffect(selectedMood) {
        dashboardViewModel.loadTodayWellness(userId)
    }

    val cards = wellnessDetails?.actionCards ?: emptyList()

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = AppBackground
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            // TOP BAR
            item {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.Default.ArrowBackIosNew,
                            contentDescription = null,
                            tint = TextPrimary
                        )
                    }

                    Text(
                        "Wellness plan for you",
                        style = MaterialTheme.typography.titleMedium,
                        color = TextPrimary
                    )
                }
            }

            wellnessDetails?.headline?.let {
                item {
                    HeroWellnessCard(it)
                }
            }

            item {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(14.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    PatternCard(
                        title = "Wellness tip",
                        text = wellnessDetails?.wellnessTip ?: "",
                        image = R.drawable.kartica1,
                        background = PeachProtein,
                        modifier = Modifier.weight(1f)
                    )

                    PatternCard(
                        title = "Rest tip",
                        text = wellnessDetails?.restTip ?: "",
                        image = R.drawable.kartica2,
                        background = OatLatte,
                        modifier = Modifier.weight(1f)
                    )
                }
            }

            if (cards.isNotEmpty()) {

                item {
                    Text(
                        "Small actions for today",
                        style = MaterialTheme.typography.titleLarge,
                        color = TextPrimary
                    )
                }

                item {
                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(14.dp),
                        contentPadding = PaddingValues(horizontal = 2.dp)
                    ) {
                        items(cards) { card ->
                            WellnessActionCardItem(card)
                        }
                    }
                }
            }
        }
    }
}


@Composable
fun HeroWellnessCard(text: String) {
    Card(
        shape = RoundedCornerShape(28.dp),
        colors = CardDefaults.cardColors(containerColor = WhiteSoft),
        elevation = CardDefaults.cardElevation(0.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(18.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    "Hello!",
                    style = MaterialTheme.typography.bodySmall,
                    color = TextSecondary
                )

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text,
                    style = MaterialTheme.typography.titleMedium,
                    color = TextPrimary,
                    fontWeight = FontWeight.SemiBold
                )
            }

            Image(
                painter = painterResource(id = R.drawable.jagodaa), // 👈 ubaci neku ilustraciju
                contentDescription = null,
                modifier = Modifier.size(90.dp),
                contentScale = ContentScale.Fit
            )
        }
    }
}

@Composable
fun PatternCard(
    title: String,
    text: String,
    image: Int,
    background: androidx.compose.ui.graphics.Color,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .aspectRatio(1f),
        shape = RoundedCornerShape(26.dp),
        colors = CardDefaults.cardColors(
            containerColor = background.copy(alpha = 0.6f)
        ),
        elevation = CardDefaults.cardElevation(0.dp)
    ) {
        Box {
            Image(
                painter = painterResource(id = image),
                contentDescription = null,
                modifier = Modifier
                    .size(160.dp)
                    .align(Alignment.BottomEnd)
                    .offset(x = 20.dp, y = 20.dp),
                contentScale = ContentScale.Fit,
                alpha = 0.25f
            )

            Column(
                modifier = Modifier.padding(18.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    title,
                    style = MaterialTheme.typography.titleMedium,
                    color = TextPrimary,
                    fontWeight = FontWeight.SemiBold
                )

                Text(
                    text,
                    style = MaterialTheme.typography.bodyMedium,
                    color = TextSecondary
                )
            }
        }
    }
}

@Composable
fun WellnessActionCardItem(
    card: com.example.activityapp.data.remote.dto.wellness.WellnessActionCardResponse
) {
    Card(
        modifier = Modifier
            .width(220.dp)
            .height(240.dp),
        shape = RoundedCornerShape(28.dp),
        colors = CardDefaults.cardColors(
            containerColor = AvocadoSmoothie.copy(alpha = 0.45f)
        ),
        elevation = CardDefaults.cardElevation(0.dp)
    ) {
        Column {

            Image(
                painter = painterResource(id = getWellnessImageRes(card.imageKey)),
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(130.dp)
                    .clip(RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp)),
                contentScale = ContentScale.Crop
            )

            Column(
                modifier = Modifier.padding(14.dp),
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Text(
                    card.title ?: "",
                    style = MaterialTheme.typography.titleMedium,
                    color = TextPrimary
                )

                Text(
                    card.description ?: "",
                    style = MaterialTheme.typography.bodySmall,
                    color = TextSecondary,
                    maxLines = 2
                )
            }
        }
    }
}