package com.yaroslavsdev.nutriscan.data.repository

import com.yaroslavsdev.nutriscan.ui.model.FoodDiaryDayUi
import com.yaroslavsdev.nutriscan.ui.model.FoodDiaryItemUi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.time.LocalDate

object FoodDiaryRepository {

    private val storage = mutableListOf<FoodDiaryItemUi>()

    fun add(item: FoodDiaryItemUi) {
        storage.add(item)
    }

    fun getDay(date: LocalDate): Flow<FoodDiaryDayUi> = flow {
        val itemsForDay = storage
            .filter { it.eatenAt.toLocalDate() == date }
            .sortedBy { it.eatenAt }

        emit(
            FoodDiaryDayUi(
                date = date,
                items = itemsForDay,
                totalCalories = itemsForDay.sumOf { it.calories.toDouble() }.toFloat()
            )
        )
    }
}