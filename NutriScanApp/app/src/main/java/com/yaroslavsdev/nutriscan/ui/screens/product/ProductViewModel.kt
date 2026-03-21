package com.yaroslavsdev.nutriscan.ui.screens.product

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yaroslavsdev.nutriscan.data.repository.ProductRepository
import com.yaroslavsdev.nutriscan.ui.state.ProductState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ProductViewModel(
    private val repository: ProductRepository
) : ViewModel() {
    private val _state = MutableStateFlow<ProductState>(ProductState.Idle)
    val state: StateFlow<ProductState> = _state

    fun loadProduct(barcode: String) {
        viewModelScope.launch {
            _state.value = ProductState.Loading

            repository.getProduct(barcode).fold(
                onSuccess = { _state.value = ProductState.Success(it) },
                onFailure = { throwable ->
                    _state.value = when (throwable.message) {
                        "NOT_FOUND" -> ProductState.NotFound
                        "NO_CONNECTION" -> ProductState.NoConnection
                        "UNAUTHORIZED" -> ProductState.Error("Сессия истекла, войдите заново")
                        else -> ProductState.Error(throwable.message ?: "Ошибка")
                    }
                }
            )
        }
    }
}