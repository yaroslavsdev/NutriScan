package com.yaroslavsdev.nutriscan.data.repository

import com.yaroslavsdev.nutriscan.data.local.TokenManager
import com.yaroslavsdev.nutriscan.data.remote.api.ProductsApi
import com.yaroslavsdev.nutriscan.data.remote.dto.toDomain
import com.yaroslavsdev.nutriscan.domain.model.Product
import com.yaroslavsdev.nutriscan.ui.model.ScannedProductUi
import okio.IOException
import retrofit2.HttpException
import java.time.ZoneId

class ProductsRepository(
    private val api: ProductsApi,
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

    suspend fun getScanHistory() : Result<List<ScannedProductUi>> {
        return try {
            val dtos = api.getScanHistory()
            val items = dtos.map { dto ->
                ScannedProductUi(
                    barcode = dto.barcode,
                    status = dto.status,
                    name = dto.name,
                    ingredients = dto.ingredients,
                    calories = dto.calories,
                    scannedAt = dto.scan_time.atZone(ZoneId.systemDefault()).toLocalDateTime()
                )
            }
            Result.success(items)
        } catch (e: HttpException) {
            when(e.code()) {
                401 -> Result.failure(Exception("UNAUTHORIZED"))
                else -> Result.failure(e)
            }
        } catch (e: IOException) {
            Result.failure(Exception("NO_CONNECTION"))
        }
    }
}