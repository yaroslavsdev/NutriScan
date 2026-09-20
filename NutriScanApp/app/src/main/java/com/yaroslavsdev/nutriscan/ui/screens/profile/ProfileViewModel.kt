package com.yaroslavsdev.nutriscan.ui.screens.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yaroslavsdev.nutriscan.data.remote.api.AuthApi
import com.yaroslavsdev.nutriscan.data.remote.dto.UserProfileDto
import com.yaroslavsdev.nutriscan.data.repository.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ProfileViewModel(
    private val authApi: AuthApi,
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _userProfile = MutableStateFlow<UserProfileDto?>(null)
    val userProfile = _userProfile.asStateFlow()

    private val _isError = MutableStateFlow(false)
    val isError = _isError.asStateFlow()

    init {
        fetchProfile()
    }

    fun fetchProfile() {
        viewModelScope.launch {
            try {
                val profile = authApi.getMe()
                _userProfile.value = profile
            } catch (e: Exception) {
                _isError.value = true
            }
        }
    }

    fun consumeError() {
        _isError.value = false
    }

    fun logout(onNavigateToAuth: () -> Unit) {
        authRepository.logout()
        onNavigateToAuth()
    }
}