package com.yaroslavsdev.nutriscan.domain.model

data class Product(
    val barcode: String,
    val name: String,
    val brand: String?,
    val ingredients: String,
    val calories: Float,
    val proteins: Float,
    val fats: Float,
    val carbs: Float
)