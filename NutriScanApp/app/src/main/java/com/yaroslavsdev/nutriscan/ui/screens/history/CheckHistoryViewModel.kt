package com.yaroslavsdev.nutriscan.ui.screens.history

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yaroslavsdev.nutriscan.data.repository.ProductsRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class CheckHistoryViewModel(
    private val repository: ProductsRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(CheckHistoryUiState())
    val uiState = _uiState.asStateFlow()

    private val _isError = MutableStateFlow(false)
    val isError = _isError.asStateFlow()

    fun loadHistory() {
        viewModelScope.launch {
            repository.getScanHistory()
                .onSuccess { items ->
                    val grouped = items
                        .sortedByDescending { it.scannedAt }
                        .groupBy { it.scannedAt.toLocalDate() }

                    _uiState.value = CheckHistoryUiState(
                        grouped = grouped,
                        isEmpty = grouped.isEmpty()
                    )
                }
                .onFailure {
                    _isError.value = true
                }
        }
    }

    fun consumeError() {
        _isError.value = false
    }
}