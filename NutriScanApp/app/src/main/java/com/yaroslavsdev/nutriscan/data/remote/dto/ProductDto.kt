package com.yaroslavsdev.nutriscan.data.remote.dto

import com.yaroslavsdev.nutriscan.domain.model.Product

data class ProductDto(
    val id: Int,
    val barcode: String,
    val name: String,
    val brand: String?,
    val ingredients: String,
    val calories: Float,
    val proteins: Float,
    val fats: Float,
    val carbs: Float
)


fun ProductDto.toDomain(): Product {
    return Product(
        barcode = barcode,
        name = name,
        brand = brand,
        ingredients = ingredients,
        calories = calories,
        proteins = proteins,
        fats = fats,
        carbs = carbs
    )
}