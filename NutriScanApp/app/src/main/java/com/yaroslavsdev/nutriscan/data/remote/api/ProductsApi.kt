package com.yaroslavsdev.nutriscan.data.remote.api

import com.yaroslavsdev.nutriscan.data.remote.dto.ProductDto
import com.yaroslavsdev.nutriscan.data.remote.dto.ScanHistoryDto
import retrofit2.http.*

interface ProductsApi {
    @GET("products/{barcode}")
    suspend fun getProduct(
        @Path("barcode") barcode: String
    ) : ProductDto

    @GET("products/history/scans")
    suspend fun getScanHistory(
        @Query("limit") limit: Int = 100
    ) : List<ScanHistoryDto>
}