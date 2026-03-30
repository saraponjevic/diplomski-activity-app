package com.example.activityapp.ui.screens.freetime

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.activityapp.data.remote.dto.freetime.FreeTimeCategoryGroupDto

@Composable
fun FreeTimeCategorySection(
    group: FreeTimeCategoryGroupDto,
    modifier: Modifier = Modifier
) {
    val sortedActivities = group.activities
        .sortedBy { it.sortOrder }
        .take(3)

    Column(
        modifier = modifier.fillMaxWidth()
    ) {
        Text(
            text = getCategoryDisplayName(group.category),
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(horizontal = 16.dp)
        )

        Spacer(modifier = Modifier.height(10.dp))

        LazyRow(
            contentPadding = PaddingValues(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(sortedActivities) { activity ->
                FreeTimeActivityCard(activity = activity)
            }
        }
    }
}