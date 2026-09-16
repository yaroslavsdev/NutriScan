package com.yaroslavsdev.nutriscan.ui.screens.diary

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yaroslavsdev.nutriscan.data.repository.DiaryRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.time.LocalDate

class FoodDiaryViewModel(
    private val repository: DiaryRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(FoodDiaryUiState())
    val uiState = _uiState.asStateFlow()

    private val _isError = MutableStateFlow(false)
    val isError = _isError.asStateFlow()

    init {
        loadDay(LocalDate.now())
    }

    fun loadDay(date: LocalDate) {
        viewModelScope.launch {
            repository.getDay(date)
                .onSuccess { _uiState.value = it }
                .onFailure { _isError.value = true }
        }
    }

    fun previousDay() {
        loadDay(_uiState.value.date.minusDays(1))
    }

    fun nextDay() {
        loadDay(_uiState.value.date.plusDays(1))
    }

    fun consumeError() {
        _isError.value = false
    }
}