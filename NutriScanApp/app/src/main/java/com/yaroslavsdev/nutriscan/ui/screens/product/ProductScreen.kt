package com.yaroslavsdev.nutriscan.ui.screens.product

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.yaroslavsdev.nutriscan.ui.components.ProductInfoCard
import com.yaroslavsdev.nutriscan.ui.navigation.Screen
import com.yaroslavsdev.nutriscan.ui.state.ProductState
import org.koin.androidx.compose.koinViewModel

@Composable
fun ProductScreen(
    barcode: String,
    navController: NavController,
    onBack: () -> Unit,
    viewModel: ProductViewModel = koinViewModel()
) {
    val state by viewModel.state.collectAsState()

    LaunchedEffect(barcode) {
        viewModel.loadProduct(barcode)
    }

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
        ) {
            IconButton(onClick = onBack) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, null)
            }
            Text("Товар", style = MaterialTheme.typography.titleLarge)
        }

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 8.dp, vertical = 10.dp),
            contentAlignment = Alignment.TopCenter
        ) {

            when (state) {

                ProductState.Idle -> {}

                ProductState.Loading -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(64.dp),
                            strokeWidth = 6.dp
                        )
                    }
                }

                is ProductState.Success -> {
                    val product = (state as ProductState.Success).product

                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .verticalScroll(rememberScrollState()),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        ProductInfoCard(
                            name = product.name,
                            brand = product.brand,
                            ingredients = product.ingredients,
                            calories = product.calories,
                            proteins = product.proteins,
                            fats = product.fats,
                            carbs = product.carbs,
                            matchedAllergens = product.matchedAllergens,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }

                ProductState.NotFound -> {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text("Продукт не найден в базе, но вы можете добавить информацию о нём")

                        Spacer(Modifier.height(12.dp))

                        Button(
                            onClick = {
                                navController.navigate(
                                    Screen.AddProductScreen.createRoute(barcode)
                                )
                            },
                            modifier = Modifier.fillMaxWidth(),
                            shape = MaterialTheme.shapes.large
                        ) {
                            Text("Добавить информацию о товаре")
                        }
                    }
                }

                ProductState.NoConnection -> {
                    Text("Нет подключения к интернету")
                }

                is ProductState.Error -> {
                    Text("Ошибка")
                }
            }
        }
    }
}