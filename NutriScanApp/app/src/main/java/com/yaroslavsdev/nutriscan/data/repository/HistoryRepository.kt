package com.yaroslavsdev.nutriscan.data.repository

import com.yaroslavsdev.nutriscan.ui.model.ScannedProductUi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

object HistoryRepository {

    private val _items = MutableStateFlow<List<ScannedProductUi>>(emptyList())

    val items = _items.asStateFlow()

    fun add(item: ScannedProductUi) {
        _items.value = listOf(item) + _items.value
    }
}