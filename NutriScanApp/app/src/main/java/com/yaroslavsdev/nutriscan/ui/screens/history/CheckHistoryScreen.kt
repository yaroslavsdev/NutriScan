package com.yaroslavsdev.nutriscan.ui.screens.history

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.yaroslavsdev.nutriscan.ui.navigation.Screen
import org.koin.androidx.compose.koinViewModel
import java.time.format.DateTimeFormatter

@Composable
fun CheckHistoryScreen(
    navController: NavHostController,
    viewModel: CheckHistoryViewModel = koinViewModel()
) {
    val context = LocalContext.current
    val state by viewModel.uiState.collectAsState()
    val isError by viewModel.isError.collectAsState()

    LaunchedEffect(isError) {
        if (isError) {
            Toast.makeText(context, "Не удалось загрузить историю", Toast.LENGTH_SHORT).show()
            viewModel.consumeError()
        }
    }

    LaunchedEffect(Unit) {
        viewModel.loadHistory()
    }

    Column(
        modifier = Modifier
            .padding(horizontal = 8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        when {
            state.isEmpty -> {
                EmptyHistory()
            }
            else -> {
                HistoryList(
                    state,
                    { barcode -> navController.navigate(Screen.ProductScreen.createRoute(barcode)) }
                    )
            }
        }
    }
}

@Composable
fun EmptyHistory() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("История проверок пуста")
    }
}


@Composable
fun HistoryList(state: CheckHistoryUiState, onItemClicked: (String) -> Unit) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        state.grouped.forEach { (date, items) ->
            item {
                Text(
                    text = date.format(
                        DateTimeFormatter.ofPattern("dd MMMM yyyy")
                    )
                )
            }

            items(items.size) { index ->
                HistoryCard(items[index], { onItemClicked(items[index].barcode) })
            }
        }
    }
}