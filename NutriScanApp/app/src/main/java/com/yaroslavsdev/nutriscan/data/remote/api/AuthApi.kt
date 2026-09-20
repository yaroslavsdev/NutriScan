package com.yaroslavsdev.nutriscan.data.remote.api

import com.yaroslavsdev.nutriscan.data.remote.dto.AllergensUpdateDto
import com.yaroslavsdev.nutriscan.data.remote.dto.AuthRequest
import com.yaroslavsdev.nutriscan.data.remote.dto.AuthResponse
import com.yaroslavsdev.nutriscan.data.remote.dto.CaloriesUpdateDto
import com.yaroslavsdev.nutriscan.data.remote.dto.PasswordUpdateDto
import com.yaroslavsdev.nutriscan.data.remote.dto.PasswordUpdateResponseDto
import com.yaroslavsdev.nutriscan.data.remote.dto.UserProfileDto
import com.yaroslavsdev.nutriscan.data.remote.dto.UsernameUpdateDto
import com.yaroslavsdev.nutriscan.data.remote.dto.UsernameUpdateResponseDto
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface AuthApi {
    @POST("auth/register")
    suspend fun register(@Body request: AuthRequest): AuthResponse

    @POST("auth/login")
    suspend fun login(@Body request: AuthRequest): AuthResponse

    @GET("auth/me")
    suspend fun getMe(): UserProfileDto

    @POST("auth/allergens")
    suspend fun saveAllergens(@Body request: AllergensUpdateDto)

    @POST("auth/calories")
    suspend fun saveCalories(@Body request: CaloriesUpdateDto)

    @POST("auth/username")
    suspend fun updateUsername(@Body request: UsernameUpdateDto): UsernameUpdateResponseDto

    @POST("auth/password")
    suspend fun updatePassword(@Body request: PasswordUpdateDto): PasswordUpdateResponseDto
}