package com.yaroslavsdev.nutriscan.ui.screens.diary

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
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
            Text(item.productName)
            Text("${item.weightGrams.toInt()} г · ${item.calories.toInt()} ккал")
            Text(item.createdAt.format(DateTimeFormatter.ofPattern("HH:mm")))
        }
    }
}