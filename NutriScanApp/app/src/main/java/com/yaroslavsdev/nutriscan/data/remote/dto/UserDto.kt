package com.yaroslavsdev.nutriscan.data.remote.dto

import com.google.gson.annotations.SerializedName

data class UserProfileDto(
    val username: String,
    val email: String,
    val allergens: List<String>,
    val dailyCalorieGoal: Int
)

data class AllergensUpdateDto(
    val allergens: List<String>
)

data class CaloriesUpdateDto(
    @SerializedName("daily_calorie_goal")
    val dailyCalorieGoal: Int
)