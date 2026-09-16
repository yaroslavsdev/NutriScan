package com.yaroslavsdev.nutriscan.data.repository

import com.yaroslavsdev.nutriscan.data.remote.api.DiaryApi
import com.yaroslavsdev.nutriscan.data.remote.dto.DiaryEntryCreateDto
import com.yaroslavsdev.nutriscan.data.remote.dto.DiaryEntryItemDto
import com.yaroslavsdev.nutriscan.ui.screens.diary.DiaryItemUi
import com.yaroslavsdev.nutriscan.ui.screens.diary.FoodDiaryUiState
import okio.IOException
import retrofit2.HttpException
import java.time.LocalDate
import java.time.ZoneId

class DiaryRepository(
    private val api: DiaryApi
) {
    suspend fun addEntry(
        barcode: String,
        mealType: String,
        weightGrams: Float,
        entryDate: LocalDate
    ): Result<Unit> {
        return try {
            api.addEntry(
                DiaryEntryCreateDto(
                barcode = barcode,
                mealType = mealType,
                weightGrams = weightGrams,
                entryDate = entryDate
            ))
            Result.success(Unit)
        } catch (e: HttpException) {
            when (e.code()) {
                401 -> Result.failure(Exception("UNAUTHORIZED"))
                404 -> Result.failure(Exception("NOT_FOUND"))
                else -> Result.failure(e)
            }
        } catch (e: IOException) {
            Result.failure(Exception("NO_CONNECTION"))
        }
    }

    suspend fun getDay(date: LocalDate): Result<FoodDiaryUiState> {
        return try {
            val dto = api.getDay(date)

            fun List<DiaryEntryItemDto>.toUi() = map {
                DiaryItemUi(
                    id = it.id,
                    productName = it.productName,
                    weightGrams = it.weightGrams,
                    calories = it.calories,
                    proteins = it.proteins,
                    fats = it.fats,
                    carbs = it.carbs,
                    createdAt = it.createdAt.atZone(ZoneId.systemDefault()).toLocalDateTime()
                )
            }

            Result.success(
                FoodDiaryUiState(
                    date = dto.date,
                    totalCalories = dto.totalCalories,
                    breakfast = dto.breakfast.toUi(),
                    lunch = dto.lunch.toUi(),
                    dinner = dto.dinner.toUi(),
                    snack = dto.snack.toUi()
                )
            )
        } catch (e: HttpException) {
            when (e.code()) {
                401 -> Result.failure(Exception("UNAUTHORIZED"))
                else -> Result.failure(e)
            }
        } catch (e: IOException) {
            Result.failure(Exception("NO_CONNECTION"))
        }
    }
}