package com.yaroslavsdev.nutriscan.data.remote.api

import com.yaroslavsdev.nutriscan.data.remote.dto.ProductCreateDto
import com.yaroslavsdev.nutriscan.data.remote.dto.ProductDto
import com.yaroslavsdev.nutriscan.data.remote.dto.ScanHistoryDto
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface ProductsApi {
    @GET("products/{barcode}")
    suspend fun getProduct(
        @Path("barcode") barcode: String
    ) : ProductDto

    @POST("products")
    suspend fun addProduct(
        @Body request: ProductCreateDto
    ): ProductDto

    @GET("products/history/scans")
    suspend fun getScanHistory(
        @Query("limit") limit: Int = 100
    ) : List<ScanHistoryDto>
}