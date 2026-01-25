package com.yaroslavsdev.nutriscan.ui.screens.history

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yaroslavsdev.nutriscan.data.repository.HistoryRepository
import kotlinx.coroutines.flow.*

class CheckHistoryViewModel : ViewModel() {

    val uiState: StateFlow<CheckHistoryUiState> =
        HistoryRepository.items
            .map { items ->
                val grouped = items
                    .sortedByDescending { it.scannedAt }
                    .groupBy { it.scannedAt.toLocalDate() }

                CheckHistoryUiState(
                    grouped = grouped,
                    isEmpty = grouped.isEmpty()
                )
            }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = CheckHistoryUiState()
            )
}