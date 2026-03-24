package com.yaroslavsdev.nutriscan.data.repository

import com.yaroslavsdev.nutriscan.data.local.TokenManager
import com.yaroslavsdev.nutriscan.data.remote.api.ProductApi
import com.yaroslavsdev.nutriscan.data.remote.dto.toDomain
import com.yaroslavsdev.nutriscan.domain.model.Product
import okio.IOException
import retrofit2.HttpException

class ProductRepository(
    private val api: ProductApi,
    private val tokenManager: TokenManager
) {
    suspend fun getProduct(barcode: String): Result<Product> {
        return try {
            val dto = api.getProduct(barcode)
            Result.success(dto.toDomain())
        } catch (e: HttpException) {
            when (e.code()) {
                401 -> Result.failure(Exception("UNAUTHORIZED"))
                404 -> Result.failure(Exception("NOT_FOUND"))
                else -> Result.failure(e)
            }
        } catch (e: IOException) {
            Result.failure(Exception("NO_CONNECTION"))
        }
    }
}