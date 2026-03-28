package com.yaroslavsdev.nutriscan.ui.screens.allergens

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yaroslavsdev.nutriscan.R
import com.yaroslavsdev.nutriscan.data.repository.AuthRepository
import com.yaroslavsdev.nutriscan.domain.model.Allergen
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class AllergensViewModel(private val repository: AuthRepository) : ViewModel() {

    private val _isError = MutableStateFlow(false)
    val isError = _isError.asStateFlow()

    private val _allergens = MutableStateFlow(listOf(
        Allergen("lactose", "Молоко", R.drawable.ic_launcher_foreground),
        Allergen("eggs", "Яйца", R.drawable.ic_launcher_foreground),
        Allergen("gluten", "Глютен", R.drawable.ic_launcher_foreground),
        Allergen("peanuts", "Арахис", R.drawable.ic_peanut),
        Allergen("tree_nuts", "Орехи", R.drawable.ic_launcher_foreground),
        Allergen("soya", "Соя", R.drawable.ic_launcher_foreground),
        Allergen("fish", "Рыба", R.drawable.ic_launcher_foreground),
        Allergen("seafood", "Морепродукты", R.drawable.ic_launcher_foreground),
        Allergen("sesame", "Кунжут", R.drawable.ic_launcher_foreground)
    ))
    val allergens = _allergens.asStateFlow()

    init { loadData() }

    fun loadData() {
        viewModelScope.launch {
            _isError.value = false
            repository.getMe()
                .onSuccess { profile ->
                    _allergens.update { list ->
                        list.map { it.copy(isSelected = profile.allergens.contains(it.id)) }
                    }
                }
                .onFailure { _isError.value = true }
        }
    }

    fun toggleAllergen(id: String) {
        _allergens.update { list ->
            list.map { if (it.id == id) it.copy(isSelected = !it.isSelected) else it }
        }
    }

    fun saveAndContinue(onSuccess: () -> Unit) {
        viewModelScope.launch {
            val selectedIds = _allergens.value.filter { it.isSelected }.map { it.id }
            repository.saveAllergens(selectedIds).onSuccess { onSuccess() }
        }
    }
}