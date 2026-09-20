package com.yaroslavsdev.nutriscan.ui.screens.editName

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yaroslavsdev.nutriscan.data.repository.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class EditNameViewModel(
    private val repository: AuthRepository
) : ViewModel() {

    private val _username = MutableStateFlow("")
    val username = _username.asStateFlow()

    private val _isSaving = MutableStateFlow(false)
    val isSaving = _isSaving.asStateFlow()

    private val _isError = MutableStateFlow(false)
    val isError = _isError.asStateFlow()

    init {
        viewModelScope.launch {
            repository.getMe().onSuccess { profile ->
                _username.value = profile.username
            }
        }
    }

    fun saveUsername(newUsername: String, onSuccess: () -> Unit) {
        if (_isSaving.value) return

        if (newUsername.isBlank()) {
            _isError.value = true
            return
        }

        _isSaving.value = true

        viewModelScope.launch {
            repository.updateUsername(newUsername)
                .onSuccess {
                    _isSaving.value = false
                    onSuccess()
                }
                .onFailure {
                    _isSaving.value = false
                    _isError.value = true
                }
        }
    }

    fun consumeError() {
        _isError.value = false
    }
}