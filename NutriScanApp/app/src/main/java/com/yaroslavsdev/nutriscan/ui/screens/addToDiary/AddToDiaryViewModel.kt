package com.yaroslavsdev.nutriscan.ui.screens.addToDiary

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yaroslavsdev.nutriscan.data.repository.DiaryRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.time.LocalDate

class AddToDiaryViewModel(
    private val repository: DiaryRepository
) : ViewModel() {
    private val _isSaving = MutableStateFlow(false)
    val isSaving = _isSaving.asStateFlow()

    private val _isError = MutableStateFlow(false)
    val isError = _isError.asStateFlow()

    fun addEntry(barcode: String, mealType: String, weightGrams: Float, onSuccess: () -> Unit) {
        if (_isSaving.value) return
        _isSaving.value = true

        viewModelScope.launch {
            repository.addEntry(barcode, mealType, weightGrams, LocalDate.now())
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