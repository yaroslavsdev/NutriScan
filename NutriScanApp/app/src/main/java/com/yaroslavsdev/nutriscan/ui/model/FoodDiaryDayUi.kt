package com.yaroslavsdev.nutriscan.ui.model

import java.time.LocalDate

data class FoodDiaryDayUi(
    val date: LocalDate,
    val items: List<FoodDiaryItemUi>,
    val totalCalories: Float
)