package com.yaroslavsdev.nutriscan.ui.screens.product

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import com.yaroslavsdev.nutriscan.data.local.TokenManager
import com.yaroslavsdev.nutriscan.data.repository.ProductRepository
import com.yaroslavsdev.nutriscan.ui.state.ProductState
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductScreen(
    barcode: String,
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
                        Icon(Icons.Default.ArrowBack, null)
                    }
                }
            )
        }
    ) { padding ->

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            contentAlignment = Alignment.Center
        ) {

            when (state) {

                ProductState.Idle -> {}

                ProductState.Loading -> {
                    CircularProgressIndicator()
                }

                is ProductState.Success -> {
                    val product = (state as ProductState.Success).product

                    Column(
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Text(product.name, style = MaterialTheme.typography.titleLarge)
                        Spacer(Modifier.height(8.dp))
                        Text("Бренд: ${product.brand ?: "—"}")
                        Text("Калории: ${product.calories}")
                        Text("Белки: ${product.proteins}")
                        Text("Жиры: ${product.fats}")
                        Text("Углеводы: ${product.carbs}")
                    }
                }

                ProductState.NotFound -> {
                    Text("Товар не найден")
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