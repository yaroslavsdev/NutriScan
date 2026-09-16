package com.yaroslavsdev.nutriscan.data.remote.api

import com.yaroslavsdev.nutriscan.data.remote.dto.DiaryDayResponseDto
import com.yaroslavsdev.nutriscan.data.remote.dto.DiaryEntryCreateDto
import com.yaroslavsdev.nutriscan.data.remote.dto.DiaryEntryItemDto
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query
import java.time.LocalDate

interface DiaryApi {
    @POST("diary")
    suspend fun addEntry(@Body request: DiaryEntryCreateDto): DiaryEntryItemDto

    @GET("diary")
    suspend fun getDay(@Query("date") date: LocalDate): DiaryDayResponseDto
}