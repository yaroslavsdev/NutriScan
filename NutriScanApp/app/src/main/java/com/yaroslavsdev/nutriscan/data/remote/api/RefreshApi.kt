package com.yaroslavsdev.nutriscan.data.remote.api

import com.yaroslavsdev.nutriscan.data.remote.dto.AuthResponse
import com.yaroslavsdev.nutriscan.data.remote.dto.RefreshRequest
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface RefreshApi {
    @POST("auth/refresh")
    fun refresh(@Body request: RefreshRequest): Call<AuthResponse>
}