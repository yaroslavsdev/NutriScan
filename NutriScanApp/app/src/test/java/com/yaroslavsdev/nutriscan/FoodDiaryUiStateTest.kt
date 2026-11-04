package com.yaroslavsdev.nutriscan

import com.yaroslavsdev.nutriscan.ui.screens.diary.DiaryItemUi
import com.yaroslavsdev.nutriscan.ui.screens.diary.FoodDiaryUiState
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import java.time.LocalDateTime

class FoodDiaryUiStateTest {
    private fun makeItem(id: Int, grams: Float): DiaryItemUi {
        return DiaryItemUi(
            barcode = "12345678",
            productName = "Продукт",
            weightGrams = grams,
            calories = 0f,
            proteins = 0f,
            fats = 0f,
            carbs = 0f,
            createdAt = LocalDateTime.now()
        )
    }

    @Test
    fun itemsJoinsAllMeals() {
        val state = FoodDiaryUiState(
            breakfast = listOf(makeItem(1, 100f)),
            lunch = listOf(makeItem(2, 200f)),
            snack = listOf(makeItem(3, 50f))
        )

        assertEquals(3, state.items().size)
    }

    @Test
    fun totalWeightSumsGrams() {
        val state = FoodDiaryUiState(
            breakfast = listOf(makeItem(1, 100f)),
            dinner = listOf(makeItem(2, 250f))
        )

        assertEquals(350f, state.totalWeight())
    }

    @Test
    fun emptyDayHasZeroWeight() {
        val state = FoodDiaryUiState()

        assertEquals(0f, state.totalWeight())
    }
}