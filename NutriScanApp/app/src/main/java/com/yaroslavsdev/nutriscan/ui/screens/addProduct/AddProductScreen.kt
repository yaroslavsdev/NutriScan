package com.yaroslavsdev.nutriscan.ui.screens.addProduct

import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import org.koin.androidx.compose.koinViewModel

@Composable
fun AddProductScreen(
    barcode: String,
    navController: NavController,
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

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Добавление товара", style = MaterialTheme.typography.headlineMedium, fontSize = 24.sp)

        Text(
            text = "Штрих-код: $barcode",
            modifier = Modifier.padding(top = 8.dp, bottom = 16.dp)
        )

        OutlinedTextField(
            value = name,
            onValueChange = { name = it },
            label = { Text("Название") },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = brand,
            onValueChange = { brand = it },
            label = { Text("Бренд (необязательно)") },
            modifier = Modifier.fillMaxWidth().padding(top = 8.dp)
        )

        OutlinedTextField(
            value = ingredients,
            onValueChange = { ingredients = it },
            label = { Text("Состав") },
            modifier = Modifier.fillMaxWidth().padding(top = 8.dp)
        )

        OutlinedTextField(
            value = calories,
            onValueChange = { calories = it },
            label = { Text("Калории") },
            modifier = Modifier.fillMaxWidth().padding(top = 8.dp)
        )

        OutlinedTextField(
            value = proteins,
            onValueChange = { proteins = it },
            label = { Text("Белки") },
            modifier = Modifier.fillMaxWidth().padding(top = 8.dp)
        )

        OutlinedTextField(
            value = fats,
            onValueChange = { fats = it },
            label = { Text("Жиры") },
            modifier = Modifier.fillMaxWidth().padding(top = 8.dp)
        )

        OutlinedTextField(
            value = carbs,
            onValueChange = { carbs = it },
            label = { Text("Углеводы") },
            modifier = Modifier.fillMaxWidth().padding(top = 8.dp)
        )

        Button(
            onClick = {
                val caloriesValue = calories.toFloatOrNull()
                val proteinsValue = proteins.toFloatOrNull()
                val fatsValue = fats.toFloatOrNull()
                val carbsValue = carbs.toFloatOrNull()

                val isValid = name.isNotBlank() &&
                        ingredients.isNotBlank() &&
                        caloriesValue != null &&
                        proteinsValue != null &&
                        fatsValue != null &&
                        carbsValue != null

                if (isValid) {
                    viewModel.saveProduct(
                        barcode = barcode,
                        name = name,
                        brand = brand,
                        ingredients = ingredients,
                        calories = caloriesValue,
                        proteins = proteinsValue,
                        fats = fatsValue,
                        carbs = carbsValue,
                        onSuccess = {
                            navController.popBackStack()
                        }
                    )
                } else {
                    Toast.makeText(context, "Заполните все поля корректно", Toast.LENGTH_SHORT).show()
                }
            },
            modifier = Modifier.fillMaxWidth().padding(top = 24.dp),
            shape = MaterialTheme.shapes.large
        ) {
            Text("Сохранить")
        }
    }
}