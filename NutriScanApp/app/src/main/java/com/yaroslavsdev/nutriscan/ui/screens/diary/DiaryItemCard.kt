package com.yaroslavsdev.nutriscan.ui.screens.diary

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.yaroslavsdev.nutriscan.ui.model.FoodDiaryItemUi
import java.time.format.DateTimeFormatter

@Composable
fun DiaryItemCard(item: FoodDiaryItemUi) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 12.dp)
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = androidx.compose.ui.Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = item.name,
                    fontWeight = FontWeight.Medium
                )
                Text(
                    text = item.eatenAt.format(
                        DateTimeFormatter.ofPattern("HH:mm")
                    ),
                    fontSize = 12.sp,
                    color = Color.Gray
                )
            }
            Text(
                text = "${item.calories.toInt()} ккал",
                fontWeight = FontWeight.Bold
            )
        }
    }
}