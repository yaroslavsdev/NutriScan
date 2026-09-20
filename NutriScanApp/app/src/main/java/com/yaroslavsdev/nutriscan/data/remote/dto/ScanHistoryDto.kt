package com.yaroslavsdev.nutriscan.data.remote.dto

import com.google.gson.annotations.SerializedName
import java.time.Instant

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
    @SerializedName("scan_time")
    val scanTime: Instant
)