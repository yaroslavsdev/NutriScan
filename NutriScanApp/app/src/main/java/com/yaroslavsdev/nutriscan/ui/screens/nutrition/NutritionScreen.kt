package com.yaroslavsdev.nutriscan.ui.screens.nutrition

import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.yaroslavsdev.nutriscan.R
import org.koin.androidx.compose.koinViewModel

@Composable
fun NutritionScreen(
    navController: NavController,
    viewModel: NutritionViewModel = koinViewModel()
) {
    val context = LocalContext.current
    val dailyCalorieGoal by viewModel.dailyCalorieGoal.collectAsState()
    val isError by viewModel.isError.collectAsState()
    val networkErrorMessage = stringResource(R.string.error_network_save)

    LaunchedEffect(isError) {
        if (isError) {
            Toast.makeText(context, networkErrorMessage, Toast.LENGTH_SHORT).show()
            viewModel.consumeError()
        }
    }

    NutritionContent(
        dailyCalorieGoal = dailyCalorieGoal,
        onCalorieChange = { value ->
            viewModel.changeCalorieGoal(value)
        },
        onDoneClick = {
            viewModel.saveAndContinue {
                navController.popBackStack()
            }
        }
    )
}

@Composable
fun NutritionContent(
    dailyCalorieGoal: Int,
    onCalorieChange: (Int) -> Unit,
    onDoneClick: () -> Unit
) {
    Column(
        Modifier
            .navigationBarsPadding()
            .padding(horizontal = 8.dp, vertical = 10.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Установите цель по потреблению калорий на день", style = MaterialTheme.typography.headlineSmall)

        Spacer(modifier = Modifier.height(30.dp))

        var text by remember {
            mutableStateOf("")
        }

        LaunchedEffect(dailyCalorieGoal) {
            text = dailyCalorieGoal.toString()
        }

        val calorieValue = text.toIntOrNull()
        val isInvalid = calorieValue == null || calorieValue !in 500..10000

        OutlinedTextField(
            value = text,
            onValueChange = { newText ->
                if (newText.length <= 5 && newText.all { it.isDigit() }) {
                    val cleaned = newText.trimStart('0')
                    text = cleaned
                    cleaned.toIntOrNull()?.let {
                        onCalorieChange(it)
                    }
                }
            },
            modifier = Modifier.fillMaxWidth(),
            label = {
                Text("Норма калорий")
            },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number
            ),
            suffix = {
                Text("ккал")
            },
            supportingText = {
                if (isInvalid) {
                    Text("Введите значение от 500 до 10000")
                }
            }
        )

        Button(
            onClick = onDoneClick,
            enabled = !isInvalid,
            modifier = Modifier.fillMaxWidth(),
            shape = MaterialTheme.shapes.large
        ) {
            Text("Готово")
        }
    }
}