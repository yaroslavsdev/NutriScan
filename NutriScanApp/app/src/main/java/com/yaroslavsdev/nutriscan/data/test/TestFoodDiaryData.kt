package com.yaroslavsdev.nutriscan.data.test

import com.yaroslavsdev.nutriscan.data.repository.FoodDiaryRepository
import com.yaroslavsdev.nutriscan.ui.model.FoodDiaryItemUi
import java.time.LocalDateTime

object TestFoodDiaryData {

    fun addTestDiaryData() {
        val now = LocalDateTime.now()

        FoodDiaryRepository.add(
            FoodDiaryItemUi("4601234567890", "Йогурт", 120f, now.minusHours(2))
        )
        FoodDiaryRepository.add(
            FoodDiaryItemUi("4601111111111", "Яблоко", 80f, now.minusHours(4))
        )
        FoodDiaryRepository.add(
            FoodDiaryItemUi("4602222222222", "Овсяная каша", 250f, now.minusHours(6))
        )
        FoodDiaryRepository.add(
            FoodDiaryItemUi("4603333333333", "Суп овощной", 180f, now.minusDays(1))
        )
    }
}
