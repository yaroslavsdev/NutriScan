package com.yaroslavsdev.nutriscan.data.test

import com.yaroslavsdev.nutriscan.data.repository.HistoryRepository
import com.yaroslavsdev.nutriscan.ui.model.ScannedProductUi
import java.time.LocalDateTime

object TestHistoryData {

    fun addTestHistory() {
        val now = LocalDateTime.now()

        val items = listOf(
            ScannedProductUi(
                barcode = "4601234567890",
                name = "Йогурт клубничный",
                ingredients = "молоко, сахар, клубника",
                calories = 95f,
                scannedAt = now.minusMinutes(10)
            ),
            ScannedProductUi(
                barcode = "4609876543210",
                name = "Шоколад молочный",
                ingredients = "какао, сахар, молоко",
                calories = 540f,
                scannedAt = now.minusHours(2)
            ),
            ScannedProductUi(
                barcode = "4601111111111",
                name = "Хлеб пшеничный",
                ingredients = "мука, вода, соль, дрожжи",
                calories = 265f,
                scannedAt = now.minusDays(1).minusMinutes(30)
            ),
            ScannedProductUi(
                barcode = "4602222222222",
                name = "Сок апельсиновый",
                ingredients = "апельсиновый сок",
                calories = 45f,
                scannedAt = now.minusDays(1).minusHours(3)
            ),
            ScannedProductUi(
                barcode = "4603333333333",
                name = "Овсяные хлопья",
                ingredients = "овёс",
                calories = 389f,
                scannedAt = now.minusDays(2)
            )
        )

        items.forEach {
            HistoryRepository.add(it)
        }
    }
}
