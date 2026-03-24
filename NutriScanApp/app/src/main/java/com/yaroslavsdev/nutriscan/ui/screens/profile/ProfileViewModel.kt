package com.yaroslavsdev.nutriscan.ui.screens.profile

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yaroslavsdev.nutriscan.data.local.TokenManager
import com.yaroslavsdev.nutriscan.data.remote.api.AuthApi
import com.yaroslavsdev.nutriscan.data.remote.dto.UserProfileDto
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ProfileViewModel(
    private val authApi: AuthApi,
    private val tokenManager: TokenManager
) : ViewModel() {

    private val _userProfile = MutableStateFlow<UserProfileDto?>(null)
    val userProfile = _userProfile.asStateFlow()

    init {
        fetchProfile()
    }

    private fun fetchProfile() {
        viewModelScope.launch {
            try {
                val profile = authApi.getMe()
                _userProfile.value = profile
            } catch (e: Exception) {

            }
        }
    }

    fun logout(onNavigateToAuth: () -> Unit) {
        tokenManager.deleteToken()
        onNavigateToAuth()
    }
}