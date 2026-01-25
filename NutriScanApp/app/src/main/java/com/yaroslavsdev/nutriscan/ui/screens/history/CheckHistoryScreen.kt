package com.yaroslavsdev.nutriscan.ui.screens.history

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import java.time.format.DateTimeFormatter

@Composable
fun CheckHistoryScreen(
    navController: NavHostController,
    viewModel: CheckHistoryViewModel = viewModel()
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        val state by viewModel.uiState.collectAsState()

        when {
            state.isEmpty -> {
                EmptyHistory()
            }
            else -> {
                HistoryList(state)
            }
        }
    }
}

@Composable
fun EmptyHistory() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text("История проверок пуста")
    }
}


@Composable
fun HistoryList(state: CheckHistoryUiState) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp)
    ) {
        state.grouped.forEach { (date, items) ->
            item {
                Text(
                    text = date.format(
                        DateTimeFormatter.ofPattern("dd MMMM yyyy")
                    ),
                    modifier = Modifier.padding(vertical = 8.dp)
                )
            }

            items(items.size) { index ->
                HistoryCard(items[index])
            }
        }
    }
}