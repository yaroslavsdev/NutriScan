package com.yaroslavsdev.nutriscan.ui.screens.addToDiary

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.FilterChip
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.yaroslavsdev.nutriscan.ui.model.MealTypeLabels
import org.koin.androidx.compose.koinViewModel

@Composable
fun AddToDiaryDialog(
    barcode: String,
    caloriesPer100: Float,
    proteinsPer100: Float,
    fatsPer100: Float,
    carbsPer100: Float,
    onDismiss: () -> Unit,
    onSaved: () -> Unit,
    viewModel: AddToDiaryViewModel = koinViewModel()
) {
    val context = LocalContext.current
    val isSaving by viewModel.isSaving.collectAsState()
    val isError by viewModel.isError.collectAsState()

    LaunchedEffect(isError) {
        if (isError) {
            Toast.makeText(context, "Не удалось добавить в дневник", Toast.LENGTH_SHORT).show()
            viewModel.consumeError()
        }
    }

    var selectedMealType by remember { mutableStateOf("breakfast") }
    var weightText by remember { mutableStateOf("100") }

    val weight = weightText.toFloatOrNull() ?: 0f
    val factor = weight / 100
    val calories = caloriesPer100 * factor
    val proteins = proteinsPer100 * factor
    val fats = fatsPer100 * factor
    val carbs = carbsPer100 * factor

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Добавить в дневник") },
        text = {
            Column {
                Text("Приём пищи")
                Spacer(Modifier.height(8.dp))

                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    MealTypeLabels.all.take(2).forEach { (key, label) ->
                        FilterChip(
                            selected = selectedMealType == key,
                            onClick = { selectedMealType = key },
                            label = { Text(label) }
                        )
                    }
                }

                Spacer(Modifier.height(8.dp))

                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    MealTypeLabels.all.drop(2).forEach { (key, label) ->
                        FilterChip(
                            selected = selectedMealType == key,
                            onClick = { selectedMealType = key },
                            label = { Text(label) }
                        )
                    }
                }

                Spacer(Modifier.height(16.dp))

                OutlinedTextField(
                    value = weightText,
                    onValueChange = { newText ->
                        if (newText.length <= 5 && newText.all { it.isDigit() }) {
                            weightText = newText
                        }
                    },
                    label = { Text("Вес порции, г") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(Modifier.height(16.dp))

                Text("Калории: ${calories.toInt()} ккал")
                Text("Белки: ${"%.1f".format(proteins)} г")
                Text("Жиры: ${"%.1f".format(fats)} г")
                Text("Углеводы: ${"%.1f".format(carbs)} г")
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    viewModel.addEntry(barcode, selectedMealType, weight) {
                        onSaved()
                    }
                },
                enabled = !isSaving && weight > 0
            ) {
                Text("Сохранить")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Отмена")
            }
        }
    )
}