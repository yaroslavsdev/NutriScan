package com.yaroslavsdev.nutriscan

import com.yaroslavsdev.nutriscan.data.repository.ProductsRepository
import com.yaroslavsdev.nutriscan.domain.model.Product
import com.yaroslavsdev.nutriscan.ui.screens.product.ProductState
import com.yaroslavsdev.nutriscan.ui.screens.product.ProductViewModel
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class ProductViewModelTest {
    private val repository = mockk<ProductsRepository>()

    @OptIn(ExperimentalCoroutinesApi::class)
    private val testDispatcher = UnconfinedTestDispatcher()

    @OptIn(ExperimentalCoroutinesApi::class)
    @BeforeEach
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @AfterEach
    fun tearDown() {
        Dispatchers.resetMain()
    }

    private fun makeProduct(): Product {
        return Product(
            barcode = "4600000000001",
            name = "Молоко",
            brand = "Бренд",
            ingredients = "молоко",
            calories = 67f,
            proteins = 3f,
            fats = 4f,
            carbs = 5f,
            matchedAllergens = emptyList()
        )
    }

    @Test
    fun loadProductSuccess() {
        coEvery { repository.getProduct("12345678", any()) } returns Result.success(makeProduct())
        val viewModel = ProductViewModel(repository)
        viewModel.loadProduct("12345678")

        assertTrue(viewModel.state.value is ProductState.Success)
    }

    @Test
    fun loadProductNotFound() {
        coEvery { repository.getProduct(any(), any()) } returns Result.failure(Exception("NOT_FOUND"))
        val viewModel = ProductViewModel(repository)
        viewModel.loadProduct("12345678")

        assertEquals(ProductState.NotFound, viewModel.state.value)
    }

    @Test
    fun loadProductNoConnection() {
        coEvery { repository.getProduct(any(), any()) } returns Result.failure(Exception("NO_CONNECTION"))
        val viewModel = ProductViewModel(repository)
        viewModel.loadProduct("12345678")

        assertEquals(ProductState.NoConnection, viewModel.state.value)
    }

    @Test
    fun sameBarcodeIsLoadedOnce() {
        var callCount = 0
        coEvery { repository.getProduct("4600000000001", any()) } answers {
            callCount++
            Result.success(makeProduct())
        }
        val viewModel = ProductViewModel(repository)
        viewModel.loadProduct("4600000000001")
        viewModel.loadProduct("4600000000001")

        assertEquals(1, callCount)
    }
}