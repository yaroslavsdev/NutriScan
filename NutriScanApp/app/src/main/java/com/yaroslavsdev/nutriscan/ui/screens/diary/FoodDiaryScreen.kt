package com.yaroslavsdev.nutriscan.ui.screens.diary

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import java.time.format.DateTimeFormatter

@Composable
fun FoodDiaryScreen(
    navController: NavHostController,
    viewModel: FoodDiaryViewModel = viewModel()
) {
    val state by viewModel.uiState.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 12.dp)
    ) {

        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth()
        ) {
            TextButton(onClick = viewModel::previousDay) {
                Text("←")
            }

            Text(
                text = state.date.format(DateTimeFormatter.ofPattern("dd.MM.yyyy")),
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp
            )

            TextButton(onClick = viewModel::nextDay) {
                Text("→")
            }
        }

        Spacer(Modifier.height(8.dp))

        Text(
            text = "Всего: ${state.totalCalories.toInt()} ккал",
            fontSize = 16.sp
        )

        Spacer(Modifier.height(16.dp))

        if (state.items.isEmpty()) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text("Нет записей за этот день")
            }
        } else {
            LazyColumn {
                items(state.items.size) {
                    DiaryItemCard(state.items[it])
                }
            }
        }
    }
}
