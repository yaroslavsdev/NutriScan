package com.yaroslavsdev.nutriscan.domain.model

data class User (
    val id: Int,
    val username: String,
    val email: String,
    val userAllergens: List<String>
)