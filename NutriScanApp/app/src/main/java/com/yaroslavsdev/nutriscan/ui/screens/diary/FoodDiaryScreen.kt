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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
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
import com.yaroslavsdev.nutriscan.ui.navigation.Screen
import org.koin.compose.viewmodel.koinViewModel
import java.time.format.DateTimeFormatter
import kotlin.math.min

@Composable
fun FoodDiaryScreen(
    navController: NavHostController,
    viewModel: FoodDiaryViewModel = koinViewModel()
) {
    val context = LocalContext.current
    val state by viewModel.uiState.collectAsState()
    val isError by viewModel.isError.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.loadCurrentData()
    }

    LaunchedEffect(isError) {
        if (isError) {
            Toast.makeText(context, "Не удалось загрузить дневник", Toast.LENGTH_SHORT).show()
            viewModel.consumeError()
        }
    }

    Column(
        modifier = Modifier
            .padding(horizontal = 10.dp)
    ) {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            IconButton(onClick = viewModel::previousDay) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, "Предыдущий день")
            }

            Text(
                text = state.date.format(DateTimeFormatter.ofPattern("dd.MM.yyyy")),
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp
            )

            IconButton(onClick = viewModel::nextDay) {
                Icon(Icons.AutoMirrored.Filled.ArrowForward, "Следующий день")
            }
        }

        Spacer(Modifier.height(8.dp))

        Text(
            text = "${state.totalCalories.toInt()} / ${state.dailyCalorieGoal} ккал",
            fontSize = 16.sp
        )

        val progress = if (state.dailyCalorieGoal == 0) {
            0f
        } else {
            min(1f * state.totalCalories / state.dailyCalorieGoal, 1f)
        }

        LinearProgressIndicator(
            progress = { progress },
            modifier = Modifier
                .fillMaxWidth()
                .height(12.dp)
        )

        Spacer(Modifier.height(12.dp))

        OutlinedButton (
            onClick = { navController.navigate(Screen.NutritionStatsScreen.createRoute(
                proteins = state.totalProteins,
                fats = state.totalFats,
                carbs = state.totalCarbs,
                totalWeight = state.totalWeight
            )) },
            modifier = Modifier.fillMaxWidth(),
            shape = MaterialTheme.shapes.large
        ) {
            Text("Статистика БЖУ")
        }

        if (state.items.isEmpty()) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text("Нет записей за этот день")
            }
        } else {
            LazyColumn {
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