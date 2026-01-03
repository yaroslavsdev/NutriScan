package com.yaroslavsdev.nutriscan.ui.screens.scan

data class ScanUiState(
    val hasPermission: Boolean = false,
    val barcode: String? = null,
    val error: String? = null
)
