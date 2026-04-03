package com.example.activityapp.ui.screens.wellnesss

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.activityapp.R
import com.example.activityapp.ui.theme.AppBackground
import com.example.activityapp.ui.theme.AvocadoSmoothie
import com.example.activityapp.ui.theme.BlushBeet
import com.example.activityapp.ui.theme.Cream
import com.example.activityapp.ui.theme.OatLatte
import com.example.activityapp.ui.theme.PeachProtein
import com.example.activityapp.ui.theme.SavorySage
import com.example.activityapp.ui.theme.TextPrimary
import com.example.activityapp.ui.theme.TextSecondary

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
        MoodOption("Happy", "HAPPY", R.drawable.jagoda),
        MoodOption("Okay", "OKAY", R.drawable.jagoda_ok),
        MoodOption("Tired", "TIRED", R.drawable.jagoda_tired),
        MoodOption("Stressed", "STRESSED", R.drawable.jagoda_stressed)
    )

    var selectedMood by remember { mutableStateOf<MoodOption?>(null) }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = AppBackground
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 22.dp, vertical = 24.dp)
        ) {


            Text(
                text = "How do you feel today?",
                style = MaterialTheme.typography.headlineMedium,
                color = TextPrimary,
                fontWeight = FontWeight.SemiBold
            )

            Spacer(modifier = Modifier.height(20.dp))

            Text(
                text = "Choose the option that best matches your mood right now.",
                style = MaterialTheme.typography.bodyLarge,
                color = TextSecondary
            )

            Spacer(modifier = Modifier.height(50.dp))

            Column(
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    MoodCard(
                        mood = moods[0],
                        isSelected = selectedMood?.value == moods[0].value,
                        onClick = { selectedMood = moods[0] },
                        modifier = Modifier.weight(1f),
                        cardColor = PeachProtein.copy(alpha = 0.55f),
                        selectedCardColor = BlushBeet.copy(alpha = 0.32f)
                    )

                    MoodCard(
                        mood = moods[1],
                        isSelected = selectedMood?.value == moods[1].value,
                        onClick = { selectedMood = moods[1] },
                        modifier = Modifier.weight(1f),
                        cardColor = OatLatte.copy(alpha = 0.58f),
                        selectedCardColor = BlushBeet.copy(alpha = 0.32f)
                    )
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    MoodCard(
                        mood = moods[2],
                        isSelected = selectedMood?.value == moods[2].value,
                        onClick = { selectedMood = moods[2] },
                        modifier = Modifier.weight(1f),
                        cardColor = OatLatte.copy(alpha = 0.58f),
                        selectedCardColor = AvocadoSmoothie.copy(alpha = 0.28f)
                    )

                    MoodCard(
                        mood = moods[3],
                        isSelected = selectedMood?.value == moods[3].value,
                        onClick = { selectedMood = moods[3] },
                        modifier = Modifier.weight(1f),
                        cardColor = PeachProtein.copy(alpha = 0.55f),
                        selectedCardColor = BlushBeet.copy(alpha = 0.30f)
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            AnimatedVisibility(visible = selectedMood != null) {
                Button(
                    onClick = {
                        selectedMood?.let { onMoodSelected(it.value) }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(54.dp),
                    shape = RoundedCornerShape(20.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = SavorySage,
                        contentColor = Color.White
                    ),
                    elevation = ButtonDefaults.buttonElevation(
                        defaultElevation = 0.dp,
                        pressedElevation = 0.dp
                    )
                ) {
                    Text(
                        text = "Continue",
                        style = MaterialTheme.typography.labelLarge,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }
    }
}

@Composable
fun MoodCard(
    mood: MoodOption,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    cardColor: Color,
    selectedCardColor: Color
) {
    val scale by animateFloatAsState(
        targetValue = if (isSelected) 1.03f else 1f,
        label = "moodCardScale"
    )

    val borderColor = if (isSelected) SavorySage else Color.Transparent
    val containerColor = if (isSelected) selectedCardColor else cardColor

    Card(
        onClick = onClick,
        modifier = modifier
            .height(166.dp)
            .scale(scale)
            .border(
                width = if (isSelected) 1.6.dp else 0.dp,
                color = borderColor,
                shape = RoundedCornerShape(24.dp)
            ),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(
            containerColor = containerColor
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 0.dp
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
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(id = mood.imageRes),
                    contentDescription = mood.label,
                    modifier = Modifier.size(86.dp),
                    contentScale = ContentScale.Fit
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = mood.label,
                style = MaterialTheme.typography.titleMedium,
                color = TextPrimary,
                fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Medium,
                maxLines = 1
            )
        }
    }
}