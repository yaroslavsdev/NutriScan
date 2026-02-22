package com.yaroslavsdev.nutriscan.ui.state

import com.yaroslavsdev.nutriscan.domain.model.Product

sealed class ProductState {
    object Idle : ProductState()
    object Loading : ProductState()
    data class Success(val product: Product) : ProductState()
    object NotFound : ProductState()
    object NoConnection : ProductState()
    data class Error(val message: String) : ProductState()
}