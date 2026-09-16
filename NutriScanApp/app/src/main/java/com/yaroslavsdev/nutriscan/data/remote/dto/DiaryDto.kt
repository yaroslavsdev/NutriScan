package com.yaroslavsdev.nutriscan.data.remote.dto

import com.google.gson.annotations.SerializedName
import java.time.Instant
import java.time.LocalDate

data class DiaryEntryCreateDto(
    val barcode: String,
    @SerializedName("meal_type")
    val mealType: String,
    @SerializedName("weight_grams")
    val weightGrams: Float,
    @SerializedName("entry_date")
    val entryDate: LocalDate
)

data class DiaryEntryItemDto(
    val id: Int,
    @SerializedName("product_name")
    val productName: String,
    @SerializedName("weight_grams")
    val weightGrams: Float,
    val calories: Float,
    val proteins: Float,
    val fats: Float,
    val carbs: Float,
    @SerializedName("created_at")
    val createdAt: Instant
)

data class DiaryDayResponseDto(
    val date: LocalDate,
    @SerializedName("total_calories")
    val totalCalories: Float,
    @SerializedName("total_proteins")
    val totalProteins: Float,
    @SerializedName("total_fats")
    val totalFats: Float,
    @SerializedName("total_carbs")
    val totalCarbs: Float,
    val breakfast: List<DiaryEntryItemDto>,
    val lunch: List<DiaryEntryItemDto>,
    val dinner: List<DiaryEntryItemDto>,
    val snack: List<DiaryEntryItemDto>
)