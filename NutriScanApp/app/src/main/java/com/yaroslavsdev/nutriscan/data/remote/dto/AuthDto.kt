package com.yaroslavsdev.nutriscan.data.remote.dto

import com.google.gson.annotations.SerializedName

data class AuthRequest(
    val email: String,
    val password: String,
    val username: String? = null
)

data class AuthResponse(
    @SerializedName("access_token")
    val accessToken: String,
    @SerializedName("token_type")
    val tokenType: String,
    @SerializedName("user_id")
    val userId: Int
)

data class UsernameUpdateDto(
    val username: String
)

data class UsernameUpdateResponseDto(
    val status: String,
    val username: String
)

data class PasswordUpdateDto(
    @SerializedName("current_password")
    val currentPassword: String,
    @SerializedName("new_password")
    val newPassword: String
)

data class PasswordUpdateResponseDto(
    val status: String
)