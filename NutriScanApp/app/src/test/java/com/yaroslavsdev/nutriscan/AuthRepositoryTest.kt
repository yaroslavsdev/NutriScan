package com.yaroslavsdev.nutriscan

import com.yaroslavsdev.nutriscan.data.local.TokenManager
import com.yaroslavsdev.nutriscan.data.remote.api.AuthApi
import com.yaroslavsdev.nutriscan.data.remote.dto.PasswordUpdateResponseDto
import com.yaroslavsdev.nutriscan.data.remote.dto.RefreshRequest
import com.yaroslavsdev.nutriscan.data.remote.dto.UsernameUpdateResponseDto
import com.yaroslavsdev.nutriscan.data.repository.AuthRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.test.runTest
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import retrofit2.HttpException
import retrofit2.Response
import java.io.IOException

class AuthRepositoryTest {
    private val api = mockk<AuthApi>()
    private val tokenManager = mockk<TokenManager>(relaxed = true)
    private val repository = AuthRepository(api, tokenManager)

    private fun makeHttpError(code: Int): HttpException {
        val body = "".toResponseBody(null)
        return HttpException(Response.error<Any>(code, body))
    }

    @Test
    fun logoutSendsRefreshTokenAndDeletesTokens() {
        runTest {
            coEvery { tokenManager.getRefreshToken() } returns "refresh123"
            coEvery { api.logout(any()) } returns Unit
            repository.logout()
            coVerify { api.logout(RefreshRequest("refresh123")) }

            verify { tokenManager.deleteTokens() }
        }
    }

    @Test
    fun logoutDeletesTokensEvenWhenNoNetwork() {
        runTest {
            coEvery { tokenManager.getRefreshToken() } returns "refresh123"
            coEvery { api.logout(any()) } throws IOException()
            repository.logout()

            verify { tokenManager.deleteTokens() }
        }
    }

    @Test
    fun logoutWithoutRefreshTokenDoesNotCallApi() {
        runTest {
            coEvery { tokenManager.getRefreshToken() } returns null
            repository.logout()

            coVerify(exactly = 0) { api.logout(any()) }
            verify { tokenManager.deleteTokens() }
        }
    }

    @Test
    fun updateUsernameSuccess() {
        runTest {
            coEvery { api.updateUsername(any()) } returns UsernameUpdateResponseDto(
                "success",
                "Иван"
            )
            val result = repository.updateUsername("Иван")

            assertTrue(result.isSuccess)
        }
    }

    @Test
    fun updateUsernameUnauthorized() {
        runTest {
            coEvery { api.updateUsername(any()) } throws makeHttpError(401)
            val result = repository.updateUsername("Иван")

            assertEquals("UNAUTHORIZED", result.exceptionOrNull()?.message)
        }
    }

    @Test
    fun updateUsernameNoConnection() {
        runTest {
            coEvery { api.updateUsername(any()) } throws IOException()

            val result = repository.updateUsername("Иван")

            assertEquals("NO_CONNECTION", result.exceptionOrNull()?.message)
        }
    }

    @Test
    fun updatePasswordSuccess() {
        runTest {
            coEvery { api.updatePassword(any()) } returns PasswordUpdateResponseDto("success")
            val result = repository.updatePassword("old12345", "new12345")

            assertTrue(result.isSuccess)
        }
    }

    @Test
    fun updatePasswordWrongCurrentPassword() {
        runTest {
            coEvery { api.updatePassword(any()) } throws makeHttpError(400)
            val result = repository.updatePassword("wrong", "new12345")

            assertEquals("WRONG_PASSWORD", result.exceptionOrNull()?.message)
        }
    }

    @Test
    fun updatePasswordUnauthorized() {
        runTest {
            coEvery { api.updatePassword(any()) } throws makeHttpError(401)
            val result = repository.updatePassword("old12345", "new12345")

            assertEquals("UNAUTHORIZED", result.exceptionOrNull()?.message)
        }
    }

    @Test
    fun updatePasswordNoConnection() {
        runTest {
            coEvery { api.updatePassword(any()) } throws IOException()
            val result = repository.updatePassword("old12345", "new12345")

            assertEquals("NO_CONNECTION", result.exceptionOrNull()?.message)
        }
    }
}