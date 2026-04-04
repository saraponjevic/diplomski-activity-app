package com.example.activityapp.ui.screens.freetime

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.activityapp.data.remote.dto.freetime.FreeTimeActivityDto
import com.example.activityapp.ui.theme.AvocadoSmoothie
import com.example.activityapp.ui.theme.BlushBeet
import com.example.activityapp.ui.theme.TextPrimary
import com.example.activityapp.ui.theme.TextSecondary
import com.example.activityapp.ui.theme.WhiteSoft

@Composable
fun FreeTimeActivityCard(
    activity: FreeTimeActivityDto,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .width(250.dp)
            .height(290.dp),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(
            containerColor = WhiteSoft
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 5.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(WhiteSoft)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp)
            ) {
                Image(
                    painter = painterResource(
                        id = getFreeTimeImageRes(
                            category = activity.category,
                            sortOrder = activity.sortOrder
                        )
                    ),
                    contentDescription = activity.title,
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp)),
                    contentScale = ContentScale.Crop
                )

                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            brush = Brush.verticalGradient(
                                colors = listOf(
                                    Color.Transparent,
                                    Color(0x16000000)
                                )
                            )
                        )
                )
            }

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 14.dp, vertical = 12.dp)
            ) {
                Text(
                    text = activity.title,
                    style = MaterialTheme.typography.titleMedium,
                    color = TextPrimary,
                    fontWeight = FontWeight.SemiBold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = activity.description,
                    style = MaterialTheme.typography.bodyMedium,
                    color = TextSecondary,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.height(12.dp))

                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Surface(
                        shape = RoundedCornerShape(50),
                        color = BlushBeet.copy(alpha = 0.35f)
                    ) {
                        Text(
                            text = getIntensityLabel(activity.intensity),
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp),
                            style = MaterialTheme.typography.bodySmall,
                            color = TextPrimary,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }

                Spacer(modifier = Modifier.weight(1f))

                Text(
                    text = "Duration: ${activity.durationMinutes} min",
                    style = MaterialTheme.typography.bodySmall,
                    color = AvocadoSmoothie,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
    }
}