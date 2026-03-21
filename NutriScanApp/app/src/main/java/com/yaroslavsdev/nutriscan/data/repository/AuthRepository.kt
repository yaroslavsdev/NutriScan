package com.yaroslavsdev.nutriscan.data.repository

import com.yaroslavsdev.nutriscan.data.local.TokenManager
import com.yaroslavsdev.nutriscan.data.remote.api.AuthApi
import com.yaroslavsdev.nutriscan.data.remote.dto.AuthRequest

class AuthRepository(
    private val api: AuthApi,
    private val tokenManager: TokenManager
) {
    suspend fun login(email: String, pass: String): Result<Unit> {
        return try {
            val response = api.login(AuthRequest(email = email, password = pass))
            tokenManager.saveToken(response.access_token)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun register(username: String, email: String, pass: String): Result<Unit> {
        return try {
            val response = api.register(
                AuthRequest(
                    username = username,
                    email = email,
                    password = pass
                )
            )
            tokenManager.saveToken(response.access_token)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}