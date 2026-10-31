package com.yaroslavsdev.nutriscan.ui.screens.addProduct

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yaroslavsdev.nutriscan.data.remote.dto.CustomProductCreateDto
import com.yaroslavsdev.nutriscan.data.remote.dto.NutritionDto
import com.yaroslavsdev.nutriscan.data.remote.dto.ProductCreateDto
import com.yaroslavsdev.nutriscan.data.repository.ProductsRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class AddProductViewModel(
    private val repository: ProductsRepository
) : ViewModel() {
    private val _isSaving = MutableStateFlow(false)
    val isSaving = _isSaving.asStateFlow()

    private val _isError = MutableStateFlow(false)
    val isError = _isError.asStateFlow()

    fun saveProduct(
        barcode: String?,
        name: String,
        brand: String,
        ingredients: String,
        calories: Float,
        proteins: Float,
        fats: Float,
        carbs: Float,
        onSuccess: (String) -> Unit
    ) {
        if (_isSaving.value) return
        _isSaving.value = true

        viewModelScope.launch {
            val nutrition = NutritionDto(calories, proteins, fats, carbs)
            val brandOrNull = brand.ifBlank { null }

            val result = if (barcode != null) {
                repository.addProduct(
                    ProductCreateDto(barcode, name, brandOrNull, ingredients, nutrition)
                )
            } else {
                repository.addCustomProduct(
                    CustomProductCreateDto(name, brandOrNull, ingredients, nutrition)
                )
            }

            result
                .onSuccess { product ->
                    _isSaving.value = false
                    onSuccess(product.barcode)
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