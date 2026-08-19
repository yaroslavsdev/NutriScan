package com.yaroslavsdev.nutriscan.data.remote.dto

import java.time.LocalDateTime

data class ScanHistoryDto(
    val id: Int,
    val status: String,
    val barcode: String,
    val name: String?,
    val brand: String?,
    val ingredients: String?,
    val calories: Float?,
    val proteins: Float?,
    val fats: Float?,
    val carbs: Float?,
    val scan_time: LocalDateTime
)