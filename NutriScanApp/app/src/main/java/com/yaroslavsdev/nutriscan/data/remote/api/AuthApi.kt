package com.yaroslavsdev.nutriscan.data.remote.api

import com.yaroslavsdev.nutriscan.data.remote.dto.AuthRequest
import com.yaroslavsdev.nutriscan.data.remote.dto.AuthResponse
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthApi {
    @POST("auth/register")
    suspend fun register(@Body request: AuthRequest): AuthResponse

    @POST("auth/login")
    suspend fun login(@Body request: AuthRequest): AuthResponse
}