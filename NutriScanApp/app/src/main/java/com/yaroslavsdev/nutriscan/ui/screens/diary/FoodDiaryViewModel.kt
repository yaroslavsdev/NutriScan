package com.yaroslavsdev.nutriscan.ui.screens.diary

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yaroslavsdev.nutriscan.data.repository.FoodDiaryRepository
import com.yaroslavsdev.nutriscan.ui.model.FoodDiaryDayUi
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import java.time.LocalDate
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn

class FoodDiaryViewModel : ViewModel() {

    private val selectedDate = MutableStateFlow<LocalDate>(LocalDate.now())

    @OptIn(ExperimentalCoroutinesApi::class)
    val uiState: StateFlow<FoodDiaryDayUi> =
        selectedDate
            .flatMapLatest { date ->
                FoodDiaryRepository.getDay(date)
            }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = FoodDiaryDayUi(
                    date = LocalDate.now(),
                    items = emptyList(),
                    totalCalories = 0f
                )
            )

    fun previousDay() {
        selectedDate.value = selectedDate.value.minusDays(1);
    }

    fun nextDay() {
        selectedDate.value = selectedDate.value.plusDays(1);
    }
}