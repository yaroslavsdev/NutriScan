package com.yaroslavsdev.nutriscan.ui.model

import java.time.LocalDateTime

data class FoodDiaryItemUi(
    val barcode: String,
    val name: String,
    val calories: Float,
    val eatenAt: LocalDateTime
)