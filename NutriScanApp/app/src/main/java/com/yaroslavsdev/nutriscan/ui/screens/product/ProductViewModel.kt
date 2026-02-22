package com.yaroslavsdev.nutriscan.ui.screens.product

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yaroslavsdev.nutriscan.data.repository.ProductRepository
import com.yaroslavsdev.nutriscan.ui.state.ProductState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ProductViewModel(
    private val repository: ProductRepository = ProductRepository()
) : ViewModel() {
    private val _state = MutableStateFlow<ProductState>(ProductState.Idle)
    val state: StateFlow<ProductState> = _state

    fun loadProduct(barcode: String) {
        viewModelScope.launch {
            _state.value = ProductState.Loading

            val result = repository.getProduct(barcode)

            _state.value = result.fold(
                onSuccess = { ProductState.Success(it) },
                onFailure = {
                    when (it.message) {
                        "NOT_FOUND" -> ProductState.NotFound
                        "NO_CONNECTION" -> ProductState.NoConnection
                        else -> ProductState.Error("Ошибка")
                    }
                }
            )
        }
    }
}