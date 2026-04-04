package com.example.activityapp.ui.screens.freetime

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.shape.GenericShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.activityapp.R
import com.example.activityapp.ui.theme.AvocadoSmoothie
import com.example.activityapp.ui.theme.TextPrimary
import com.example.activityapp.ui.theme.TextSecondary

@Composable
fun FreeTimeMainSuggestionCard(
    text: String,
    modifier: Modifier = Modifier
) {

    // 👇 blob shape kao na slici
    val blobShape = GenericShape { size, _ ->
        moveTo(0f, size.height * 0.2f)
        quadraticBezierTo(
            size.width * 0.5f,
            0f,
            size.width,
            size.height * 0.2f
        )
        quadraticBezierTo(
            size.width * 1.1f,
            size.height * 0.6f,
            size.width * 0.7f,
            size.height
        )
        quadraticBezierTo(
            size.width * 0.2f,
            size.height * 1.1f,
            0f,
            size.height * 0.8f
        )
        close()
    }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(160.dp)
    ) {

        // 🎨 blob background
        Box(
            modifier = Modifier
                .matchParentSize()
                .clip(blobShape)
                .background(Color(0xFFFFE9DF)) // peach/blush boja kao na slici
        )

        // 🧠 tekst levo
        Column(
            modifier = Modifier
                .align(Alignment.CenterStart)
                .padding(start = 20.dp, end = 120.dp) // ostavi mesta za sliku
        ) {
            Text(
                text = "Main suggestion",
                style = MaterialTheme.typography.titleMedium,
                color = AvocadoSmoothie,
                fontWeight = FontWeight.SemiBold
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = text,
                style = MaterialTheme.typography.bodyMedium,
                color = TextPrimary
            )

            Spacer(modifier = Modifier.height(6.dp))

        }

        Image(
            painter = painterResource(id = R.drawable.jagodaa),
            contentDescription = "Free time illustration",
            modifier = Modifier
                .size(110.dp)
                .align(Alignment.CenterEnd)
                .offset(x = (-10).dp),
            contentScale = ContentScale.Fit
        )
    }
}