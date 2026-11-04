package com.yaroslavsdev.nutriscan

import com.yaroslavsdev.nutriscan.data.remote.api.ProductsApi
import com.yaroslavsdev.nutriscan.data.remote.dto.ProductDto
import com.yaroslavsdev.nutriscan.data.repository.ProductsRepository
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import retrofit2.HttpException
import retrofit2.Response
import java.io.IOException

class ProductsRepositoryTest {
    private val api = mockk<ProductsApi>()
    private val repository = ProductsRepository(api)

    private fun makeHttpError(code: Int): HttpException {
        val body = "".toResponseBody("text/plain".toMediaType())
        return HttpException(Response.error<Any>(code, body))
    }

    private fun makeDto(): ProductDto {
        return ProductDto(
            id = 1,
            barcode = "4600000000001",
            name = "Молоко",
            brand = "Бренд",
            ingredients = "молоко",
            calories = 67f,
            proteins = 3f,
            fats = 4f,
            carbs = 5.5f,
            matchedAllergens = listOf("lactose")
        )
    }

    @Test
    fun getProductSuccess() {
        runTest {
            coEvery { api.getProduct("4600000000001", any()) } returns makeDto()
            val result = repository.getProduct("4600000000001")

            assertTrue(result.isSuccess)
            assertEquals("Молоко", result.getOrNull()?.name)
        }
    }

    @Test
    fun getProductNotFound() {
        runTest {
            coEvery { api.getProduct(any(), any()) } throws makeHttpError(404)
            val result = repository.getProduct("00000000")

            assertEquals("NOT_FOUND", result.exceptionOrNull()?.message)
        }
    }

    @Test
    fun getProductUnauthorized() {
        runTest {
            coEvery { api.getProduct(any(), any()) } throws makeHttpError(401)
            val result = repository.getProduct("00000000")

            assertEquals("UNAUTHORIZED", result.exceptionOrNull()?.message)
        }
    }

    @Test
    fun getProductNoConnection() {
        runTest {
            coEvery { api.getProduct(any(), any()) } throws IOException()
            val result = repository.getProduct("00000000")

            assertEquals("NO_CONNECTION", result.exceptionOrNull()?.message)
        }
    }
}