package com.yaroslavsdev.nutriscan.data.remote.api

import com.yaroslavsdev.nutriscan.data.remote.dto.ProductDto
import retrofit2.http.*

interface ProductApi {
    @GET("products/{barcode}")
    suspend fun getProduct(
        @Path("barcode") barcode: String
    ) : ProductDto
}