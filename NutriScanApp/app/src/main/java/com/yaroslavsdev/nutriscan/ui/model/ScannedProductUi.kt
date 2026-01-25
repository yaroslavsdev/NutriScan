package com.yaroslavsdev.nutriscan.ui.model

import java.time.LocalDateTime

data class ScannedProductUi(
    val barcode: String,
    val name: String,
    val ingredients: String,
    val calories: Float,
    val scannedAt: LocalDateTime
)