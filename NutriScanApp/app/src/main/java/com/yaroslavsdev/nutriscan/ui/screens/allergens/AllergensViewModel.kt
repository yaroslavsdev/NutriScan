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

class AllergensViewModel(
    private val repository: AuthRepository
) : ViewModel() {

    private val _allergens = MutableStateFlow(listOf(
        Allergen("lactose", "Лактоза", R.drawable.ic_launcher_background),
        //Allergen("peanuts", "Арахис", R.drawable.ic_peanut),
        //Allergen("gluten", "Глютен", R.drawable.ic_wheat),
    ))
    val allergens = _allergens.asStateFlow()

    fun toggleAllergen(id: String) {
        _allergens.update { list ->
            list.map { if (it.id == id) it.copy(isSelected = !it.isSelected) else it }
        }
    }

    fun saveAndContinue(onSuccess: () -> Unit) {
        val selectedIds = _allergens.value.filter { it.isSelected }.map { it.id }
        // Вызов репозитория для сохранения на сервер
        viewModelScope.launch {
            // repository.saveAllergens(selectedIds)
            onSuccess()
        }
    }
}