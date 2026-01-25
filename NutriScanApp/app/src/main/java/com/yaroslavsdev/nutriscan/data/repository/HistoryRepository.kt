package com.yaroslavsdev.nutriscan.data.repository

import com.yaroslavsdev.nutriscan.domain.model.ScanHistoryItem
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

object HistoryRepository {

    private val _items = MutableStateFlow<List<ScanHistoryItem>>(emptyList())
    val items = _items.asStateFlow()

    fun add(item: ScanHistoryItem) {
        _items.value = listOf(item) + _items.value
    }
}