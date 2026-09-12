package com.yaroslavsdev.nutriscan.ui.screens.product

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.Button
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

@OptIn(ExperimentalMaterial3Api::class)
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

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Товар") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, null)
                    }
                }
            )
        }
    ) { padding ->

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            contentAlignment = Alignment.TopCenter
        ) {

            when (state) {

                ProductState.Idle -> {}

                ProductState.Loading -> {
                    CircularProgressIndicator()
                }

                is ProductState.Success -> {
                    val product = (state as ProductState.Success).product

                    ProductInfoCard(
                        name = product.name,
                        brand = product.brand,
                        ingredients = product.ingredients,
                        calories = product.calories,
                        proteins = product.proteins,
                        fats = product.fats,
                        carbs = product.carbs,
                        matchedAllergens = product.matchedAllergens,
                        modifier = Modifier.padding(16.dp)
                    )
                }

                ProductState.NotFound -> {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.padding(horizontal = 16.dp)
                    ) {
                        Text("Продукт не найден в базе, но вы можете самостоятельно добавить информацию о нём")
                        Spacer(Modifier.height(12.dp))
                        Button(
                            onClick = { navController.navigate(Screen.AddProductScreen.createRoute(barcode)) },
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