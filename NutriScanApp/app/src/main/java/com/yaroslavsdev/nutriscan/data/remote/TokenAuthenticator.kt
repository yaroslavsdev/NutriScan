package com.yaroslavsdev.nutriscan.data.remote

import com.yaroslavsdev.nutriscan.data.local.TokenManager
import com.yaroslavsdev.nutriscan.data.remote.api.RefreshApi
import com.yaroslavsdev.nutriscan.data.remote.dto.AuthResponse
import com.yaroslavsdev.nutriscan.data.remote.dto.RefreshRequest
import okhttp3.Authenticator
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException

class TokenAuthenticator(
    private val tokenManager: TokenManager,
    baseUrl: String
) : Authenticator {
    private val refreshApi = Retrofit.Builder()
        .baseUrl(baseUrl)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(RefreshApi::class.java)

    @Synchronized
    override fun authenticate(route: Route?, response: Response): Request? {
        if (response.priorResponse != null) {
            return null
        }

        val oldHeader = response.request.header("Authorization")
        val usedToken = oldHeader?.removePrefix("Bearer ")
        val savedToken = tokenManager.getToken()

        val tokenUpdated = savedToken != null && savedToken != usedToken
        if (tokenUpdated) {
            return buildRequest(response, savedToken!!)
        }

        val refreshToken = tokenManager.getRefreshToken()
        if (refreshToken == null) {
            return null
        }

        val newTokens = updateTokens(refreshToken)
        if (newTokens == null) {
            return null
        }

        tokenManager.saveTokens(newTokens.accessToken, newTokens.refreshToken)
        return buildRequest(response, newTokens.accessToken)
    }

    private fun updateTokens(refreshToken: String): AuthResponse? {
        val call = refreshApi.refresh(RefreshRequest(refreshToken))

        val res = try {
            call.execute()
        } catch (e: IOException) {
            return null
        }

        if (res.isSuccessful) {
            return res.body()
        }

        if (res.code() == 401) {
            tokenManager.deleteTokens()
        }
        return null
    }

    private fun buildRequest(response: Response, token: String): Request {
        return response.request.newBuilder()
            .header("Authorization", "Bearer $token")
            .build()
    }
}