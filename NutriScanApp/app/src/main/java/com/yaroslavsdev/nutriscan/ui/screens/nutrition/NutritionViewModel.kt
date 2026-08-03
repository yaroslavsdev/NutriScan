package com.yaroslavsdev.nutriscan.ui.screens.nutrition

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yaroslavsdev.nutriscan.data.repository.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class NutritionViewModel(
    private val repository: AuthRepository
) : ViewModel() {

    private val _isError = MutableStateFlow(false)
    val isError = _isError.asStateFlow()
    private val _isSaving = MutableStateFlow(false)
    val isSaving = _isSaving.asStateFlow()

    private val _dailyCalorieGoal = MutableStateFlow(0)
    val dailyCalorieGoal = _dailyCalorieGoal.asStateFlow()

    init { loadData() }

    fun loadData() {
        viewModelScope.launch {
            _isError.value = false
            repository.getMe()
                .onSuccess { profile ->
                    _dailyCalorieGoal.value = profile.dailyCalorieGoal
                }
                .onFailure {
                    _isError.value = true
                }
        }
    }

    fun changeCalorieGoal(limit: Int) {
        if (limit in 500..10000) {
            _dailyCalorieGoal.value = limit
        }
    }

    fun saveAndContinue(onSuccess: () -> Unit) {
        if (_isSaving.value) return
        _isSaving.value = true

        viewModelScope.launch {
            repository.saveCalorieGoal(_dailyCalorieGoal.value)
                .onSuccess {
                    _isSaving.value = false
                    onSuccess()
                }
                .onFailure {
                    _isSaving.value = false
                    _isError.value = true
                }
        }
    }

    fun consumeError() {
        _isError.value = false
    }
}