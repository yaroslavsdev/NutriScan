package com.yaroslavsdev.nutriscan.data.local

import android.content.Context
import androidx.core.content.edit
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey

class TokenManager(context: Context) {
    private val masterKey = MasterKey.Builder(context)
        .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
        .build()

    private val prefs = EncryptedSharedPreferences.create(
        context,
        "secret_shared_prefs",
        masterKey,
        EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
        EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
    )

    fun saveTokens(accessToken: String, refreshToken: String) = prefs.edit {
        putString("jwt_token", accessToken)
        putString("refresh_token", refreshToken)
    }
    fun getToken(): String? = prefs.getString("jwt_token", null)

    fun getRefreshToken(): String? = prefs.getString("refresh_token", null)

    fun deleteTokens() = prefs.edit {
        remove("jwt_token")
        remove("refresh_token")
    }
}