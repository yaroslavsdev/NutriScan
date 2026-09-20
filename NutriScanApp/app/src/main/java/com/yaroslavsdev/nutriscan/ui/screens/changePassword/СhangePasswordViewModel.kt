package com.yaroslavsdev.nutriscan.ui.screens.changePassword

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yaroslavsdev.nutriscan.data.repository.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ChangePasswordViewModel(
    private val repository: AuthRepository
) : ViewModel() {

    private val _isSaving = MutableStateFlow(false)
    val isSaving = _isSaving.asStateFlow()

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage = _errorMessage.asStateFlow()

    fun changePassword(
        currentPassword: String,
        newPassword: String,
        onSuccess: () -> Unit
    ) {
        if (_isSaving.value) return
        _isSaving.value = true

        viewModelScope.launch {
            repository.updatePassword(currentPassword, newPassword)
                .onSuccess {
                    _isSaving.value = false
                    onSuccess()
                }
                .onFailure { throwable ->
                    _isSaving.value = false
                    _errorMessage.value = when (throwable.message) {
                        "WRONG_PASSWORD" -> "Неверный текущий пароль"
                        "NO_CONNECTION" -> "Нет подключения к интернету"
                        "UNAUTHORIZED" -> "Сессия истекла, войдите заново"
                        else -> "Не удалось сменить пароль"
                    }
                }
        }
    }

    fun consumeError() {
        _errorMessage.value = null
    }
}