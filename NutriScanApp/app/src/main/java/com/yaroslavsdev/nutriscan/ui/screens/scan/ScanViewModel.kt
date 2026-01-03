package com.yaroslavsdev.nutriscan.ui.screens.scan

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class ScanViewModel : ViewModel() {

    private val _state = MutableStateFlow(ScanUiState())
    val state = _state.asStateFlow()

    fun onPermissionResult(granted: Boolean) {
        _state.update {
            it.copy(hasPermission = granted)
        }
    }

    fun onBarcodeDetected(value: String) {
        if (_state.value.barcode != null) return

        _state.update {
            it.copy(barcode = value)
        }
    }

    fun reset() {
        _state.value = ScanUiState(hasPermission = true)
    }
}
