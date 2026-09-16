package com.yaroslavsdev.nutriscan.ui.screens.diary

import java.time.LocalDate
import java.time.LocalDateTime

data class DiaryItemUi(
    val id: Int,
    val productName: String,
    val weightGrams: Float,
    val calories: Float,
    val proteins: Float,
    val fats: Float,
    val carbs: Float,
    val createdAt: LocalDateTime
)

data class FoodDiaryUiState(
    val date: LocalDate = LocalDate.now(),
    val totalCalories: Float = 0f,
    val breakfast: List<DiaryItemUi> = emptyList(),
    val lunch: List<DiaryItemUi> = emptyList(),
    val dinner: List<DiaryItemUi> = emptyList(),
    val snack: List<DiaryItemUi> = emptyList()
) {
    val items: List<DiaryItemUi>
        get() = breakfast + lunch + dinner + snack
}