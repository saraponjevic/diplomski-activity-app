package com.example.activityapp.ui.screens.wellnesss

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import com.example.activityapp.R
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource

import androidx.compose.ui.unit.dp

data class MoodOption(
    val label: String,
    val value: String,
    val imageRes: Int
)

@Composable
fun WellnessMoodSelectionScreen(
    onMoodSelected: (String) -> Unit
) {
    val moods = listOf(
        MoodOption("Happy", "HAPPY", R.drawable.happy),
        MoodOption("Okay", "OKAY", R.drawable.okey),
        MoodOption("Tired", "TIRED", R.drawable.sad),
        MoodOption("Stressed", "STRESSED", R.drawable.stressed)
    )

    var selectedMood by remember { mutableStateOf<MoodOption?>(null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp)
    ) {
        Text(
            text = "Wellness",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(20.dp))

        Text(
            text = "How do you feel today?",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.SemiBold
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Choose the option that best matches your mood right now.",
            style = MaterialTheme.typography.bodyMedium
        )

        Spacer(modifier = Modifier.height(28.dp))

        Column(
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                MoodCard(
                    mood = moods[0],
                    isSelected = selectedMood?.value == moods[0].value,
                    onClick = { selectedMood = moods[0] },
                    modifier = Modifier.weight(1f)
                )

                MoodCard(
                    mood = moods[1],
                    isSelected = selectedMood?.value == moods[1].value,
                    onClick = { selectedMood = moods[1] },
                    modifier = Modifier.weight(1f)
                )
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                MoodCard(
                    mood = moods[2],
                    isSelected = selectedMood?.value == moods[2].value,
                    onClick = { selectedMood = moods[2] },
                    modifier = Modifier.weight(1f)
                )

                MoodCard(
                    mood = moods[3],
                    isSelected = selectedMood?.value == moods[3].value,
                    onClick = { selectedMood = moods[3] },
                    modifier = Modifier.weight(1f)
                )
            }
        }

        Spacer(modifier = Modifier.height(28.dp))

        AnimatedVisibility(visible = selectedMood != null) {
            Button(
                onClick = {
                    selectedMood?.let {
                        onMoodSelected(it.value)
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Continue")
            }
        }
    }
}

@Composable
fun MoodCard(
    mood: MoodOption,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val scale by animateFloatAsState(
        targetValue = if (isSelected) 1.04f else 1f,
        label = "cardScale"
    )

    val borderColor =
        if (isSelected) MaterialTheme.colorScheme.primary else Color.Transparent

    val containerColor =
        if (isSelected) MaterialTheme.colorScheme.primary.copy(alpha = 0.12f)
        else MaterialTheme.colorScheme.surfaceVariant

    Card(
        onClick = onClick,
        modifier = modifier
            .height(170.dp)
            .scale(scale)
            .border(
                width = 2.dp,
                color = borderColor,
                shape = RoundedCornerShape(24.dp)
            ),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(
            containerColor = containerColor
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 12.dp, vertical = 14.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .size(88.dp)
                    .clip(CircleShape)
                    .background(Color.White)
                    .padding(8.dp),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(id = mood.imageRes),
                    contentDescription = mood.label,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
            }

            Spacer(modifier = Modifier.height(14.dp))

            Text(
                text = mood.label,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
                maxLines = 1
            )
        }
    }
}

@Composable
fun MoodSelectionCard(
    mood: MoodOption,
    onClick: () -> Unit
) {
    Card(
        onClick = onClick,
        modifier = Modifier
            .width(170.dp)
            .height(220.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {


            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = mood.label,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
        }
    }
}