package com.yaroslavsdev.nutriscan.data.remote.dto

data class UserProfileDto(
    val username: String,
    val email: String,
    val allergens: List<String>
)

data class AllergensUpdateDto(
    val allergens: List<String>
)