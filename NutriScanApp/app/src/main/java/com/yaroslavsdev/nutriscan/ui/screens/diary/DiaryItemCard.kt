package com.yaroslavsdev.nutriscan.ui.screens.diary

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import java.time.format.DateTimeFormatter

@Composable
fun DiaryItemCard(item: DiaryItemUi) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 8.dp)
    ) {
        Column(Modifier.padding(12.dp)) {
            Text(
                text = item.productName,
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.titleMedium
            )

            Text("${item.weightGrams.toInt()} г, ${item.calories.toInt()} ккал")

            Spacer(Modifier.height(8.dp))

            Text(
                item.createdAt.format(DateTimeFormatter.ofPattern("HH:mm")),
                style = MaterialTheme.typography.bodySmall,
                color = Color.Gray)
        }
    }
}