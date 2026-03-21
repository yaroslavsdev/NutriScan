package com.yaroslavsdev.nutriscan.data.remote.dto

data class AuthRequest(
    val email: String,
    val password: String,
    val username: String? = null
)

data class AuthResponse(
    val access_token: String,
    val token_type: String,
    val user_id: Int
)