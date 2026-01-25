package com.yaroslavsdev.nutriscan.domain.model

data class Product (
    val barcode: String,
    val name: String,
    val ingredients: String,
    val calories: Float
)