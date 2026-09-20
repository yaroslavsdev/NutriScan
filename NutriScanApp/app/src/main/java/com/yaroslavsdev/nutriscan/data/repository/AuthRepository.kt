package com.yaroslavsdev.nutriscan.data.repository

import com.yaroslavsdev.nutriscan.data.local.TokenManager
import com.yaroslavsdev.nutriscan.data.remote.api.AuthApi
import com.yaroslavsdev.nutriscan.data.remote.dto.AllergensUpdateDto
import com.yaroslavsdev.nutriscan.data.remote.dto.AuthRequest
import com.yaroslavsdev.nutriscan.data.remote.dto.CaloriesUpdateDto
import com.yaroslavsdev.nutriscan.data.remote.dto.PasswordUpdateDto
import com.yaroslavsdev.nutriscan.data.remote.dto.UserProfileDto
import com.yaroslavsdev.nutriscan.data.remote.dto.UsernameUpdateDto
import retrofit2.HttpException
import java.io.IOException

class AuthRepository(
    private val api: AuthApi,
    private val tokenManager: TokenManager
) {
    suspend fun login(email: String, pass: String): Result<Unit> {
        return try {
            val response = api.login(AuthRequest(email = email, password = pass))
            tokenManager.saveToken(response.accessToken)
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
            tokenManager.saveToken(response.accessToken)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getMe(): Result<UserProfileDto> {
        return try {
            Result.success(api.getMe())
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun saveAllergens(ids: List<String>): Result<Unit> {
        return try {
            api.saveAllergens(AllergensUpdateDto(ids))
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun saveCalorieGoal(calorieGoal: Int): Result<Unit> {
        return try {
            api.saveCalories(CaloriesUpdateDto(calorieGoal))
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    fun logout() {
        tokenManager.deleteToken()
    }

    suspend fun updateUsername(username: String): Result<Unit> {
        return try {
            api.updateUsername(UsernameUpdateDto(username))
            Result.success(Unit)
        } catch (e: HttpException) {
            when (e.code()) {
                401 -> Result.failure(Exception("UNAUTHORIZED"))
                else -> Result.failure(e)
            }
        } catch (e: IOException) {
            Result.failure(Exception("NO_CONNECTION"))
        }
    }

    suspend fun updatePassword(currentPassword: String, newPassword: String): Result<Unit> {
        return try {
            api.updatePassword(PasswordUpdateDto(currentPassword, newPassword))
            Result.success(Unit)
        } catch (e: HttpException) {
            when (e.code()) {
                400 -> Result.failure(Exception("WRONG_PASSWORD"))
                401 -> Result.failure(Exception("UNAUTHORIZED"))
                else -> Result.failure(e)
            }
        } catch (e: IOException) {
            Result.failure(Exception("NO_CONNECTION"))
        }
    }
}