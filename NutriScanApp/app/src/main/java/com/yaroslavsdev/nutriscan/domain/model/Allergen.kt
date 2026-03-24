package com.yaroslavsdev.nutriscan.domain.model

data class Allergen(
    val id: String,
    val name: String,
    val iconRes: Int,
    val isSelected: Boolean = false
)