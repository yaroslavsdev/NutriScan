package com.yaroslavsdev.nutriscan.ui.screens.diary

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import org.koin.compose.viewmodel.koinViewModel
import java.time.format.DateTimeFormatter

@Composable
fun FoodDiaryScreen(
    navController: NavHostController,
    viewModel: FoodDiaryViewModel = koinViewModel()
) {
    val context = LocalContext.current
    val state by viewModel.uiState.collectAsState()
    val isError by viewModel.isError.collectAsState()

    LaunchedEffect(isError) {
        if (isError) {
            Toast.makeText(context, "Не удалось загрузить дневник", Toast.LENGTH_SHORT).show()
            viewModel.consumeError()
        }
    }

    Column(
        modifier = Modifier
            .statusBarsPadding()
            .padding(horizontal = 8.dp, vertical = 10.dp)
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
            LazyColumn(
                modifier = Modifier.verticalScroll(rememberScrollState())
            ) {
                mealSection("Завтрак", state.breakfast)
                mealSection("Обед", state.lunch)
                mealSection("Ужин", state.dinner)
                mealSection("Перекус", state.snack)
            }
        }
    }
}

private fun androidx.compose.foundation.lazy.LazyListScope.mealSection(
    title: String,
    entries: List<DiaryItemUi>
) {
    if (entries.isEmpty()) return

    item {
        Text(
            text = title,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(vertical = 8.dp)
        )
    }

    items(entries) { entry ->
        DiaryItemCard(entry)
    }
}
