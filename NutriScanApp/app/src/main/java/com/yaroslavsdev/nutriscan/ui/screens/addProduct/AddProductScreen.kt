package com.yaroslavsdev.nutriscan.ui.screens.addProduct

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.yaroslavsdev.nutriscan.ui.navigation.Screen
import org.koin.androidx.compose.koinViewModel

private fun String.toNutritionFloat(): Float? {
    val value = trim()
        .replace(',', '.')
        .toFloatOrNull()

    if (value == null || value < 0f) {
        return null
    }

    return value
}

@Composable
fun AddProductScreen(
    barcode: String?,
    navController: NavController,
    onBack: () -> Unit,
    viewModel: AddProductViewModel = koinViewModel()
) {
    val context = LocalContext.current
    val isError by viewModel.isError.collectAsState()

    LaunchedEffect(isError) {
        if (isError) {
            Toast.makeText(context, "Не удалось сохранить товар", Toast.LENGTH_SHORT).show()
            viewModel.consumeError()
        }
    }

    var name by remember { mutableStateOf("") }
    var brand by remember { mutableStateOf("") }
    var ingredients by remember { mutableStateOf("") }
    var calories by remember { mutableStateOf("") }
    var proteins by remember { mutableStateOf("") }
    var fats by remember { mutableStateOf("") }
    var carbs by remember { mutableStateOf("") }

    var showErrors by remember { mutableStateOf(false) }

    val isNameValid = name.isNotBlank()
    val isIngredientsValid = ingredients.isNotBlank()

    val isCaloriesValid = calories.toNutritionFloat() != null
    val isProteinsValid = proteins.toNutritionFloat() != null
    val isFatsValid = fats.toNutritionFloat() != null
    val isCarbsValid = carbs.toNutritionFloat() != null

    val isFormValid = isNameValid && isIngredientsValid
            && isCaloriesValid && isProteinsValid && isFatsValid && isCarbsValid

    Column(modifier = Modifier.fillMaxSize()) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            IconButton(onClick = onBack) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, "Кнопка назад")
            }

            Text(
                "Добавление товара",
                style = MaterialTheme.typography.headlineMedium,
                fontSize = 24.sp
            )
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(
                text = if (barcode != null) "Штрих-код: $barcode" else "Товар без штрих-кода",
                modifier = Modifier.padding(bottom = 8.dp),
                style = MaterialTheme.typography.bodyLarge
            )

            OutlinedTextField(
                value = name,
                onValueChange = { name = it
                    showErrors = false },
                label = { Text("Название") },
                isError = showErrors && !isNameValid,
                supportingText = {
                    if (showErrors && !isNameValid) Text("Название не может быть пустым")
                },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = brand,
                onValueChange = { brand = it },
                label = { Text("Бренд (необязательно)") },
                supportingText = { },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = ingredients,
                onValueChange = { ingredients = it
                    showErrors = false },
                label = { Text("Состав") },
                isError = showErrors && !isIngredientsValid,
                supportingText = {
                    if (showErrors && !isIngredientsValid) Text("Укажите состав продукта")
                },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = calories,
                onValueChange = { input ->
                    val normalized = input.replace(',', '.')
                    val isValid = normalized.count { it == '.' } <= 1 && normalized.all { it.isDigit() || it == '.' }
                    if (isValid) {
                        calories = input
                    }
                    showErrors = false
                },
                label = { Text("Калории (на 100г)") },
                isError = showErrors && !isCaloriesValid,
                supportingText = {
                    if (showErrors && !isCaloriesValid) Text("Введите число")
                },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = proteins,
                onValueChange = { input ->
                    val normalized = input.replace(',', '.')
                    val isValid = normalized.count { it == '.' } <= 1 && normalized.all { it.isDigit() || it == '.' }
                    if (isValid) {
                        proteins = input
                    }
                    showErrors = false
                },
                label = { Text("Белки (на 100г)") },
                isError = showErrors && !isProteinsValid,
                supportingText = {
                    if (showErrors && !isProteinsValid) Text("Введите число")
                },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = fats,
                onValueChange = { input ->
                    val normalized = input.replace(',', '.')
                    val isValid = normalized.count { it == '.' } <= 1 && normalized.all { it.isDigit() || it == '.' }
                    if (isValid) {
                        fats = input
                    }
                    showErrors = false
                },
                label = { Text("Жиры (на 100г)") },
                isError = showErrors && !isFatsValid,
                supportingText = {
                    if (showErrors && !isFatsValid) Text("Введите число")
                },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = carbs,
                onValueChange = { input ->
                    val normalized = input.replace(',', '.')
                    val isValid = normalized.count { it == '.' } <= 1 && normalized.all { it.isDigit() || it == '.' }
                    if (isValid) {
                        carbs = input
                    }
                    showErrors = false
                },
                label = { Text("Углеводы (на 100г)") },
                isError = showErrors && !isCarbsValid,
                supportingText = {
                    if (showErrors && !isCarbsValid) Text("Введите число")
                },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth()
            )

            Button(
                onClick = {
                    if (isFormValid) {
                        viewModel.saveProduct(
                            barcode = barcode,
                            name = name,
                            brand = brand,
                            ingredients = ingredients,
                            calories = calories.toNutritionFloat()!!,
                            proteins = proteins.toNutritionFloat()!!,
                            fats = fats.toNutritionFloat()!!,
                            carbs = carbs.toNutritionFloat()!!,
                            onSuccess = { savedBarcode ->
                                navController.navigate(Screen.ProductScreen.createRoute(savedBarcode)) {
                                    if (barcode != null) {
                                        popUpTo(Screen.ProductScreen.route) { inclusive = true }
                                    } else {
                                        popUpTo(Screen.AddCustomProductScreen.route) { inclusive = true }
                                    }
                                }
                            }
                        )
                    } else {
                        showErrors = true
                    }
                },
                modifier = Modifier.fillMaxWidth().padding(top = 24.dp),
                shape = MaterialTheme.shapes.large
            ) {
                Text("Сохранить")
            }
        }
    }
}