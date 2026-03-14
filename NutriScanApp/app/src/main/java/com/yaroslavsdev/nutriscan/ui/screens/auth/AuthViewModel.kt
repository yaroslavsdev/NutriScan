package com.yaroslavsdev.nutriscan.ui.screens.auth

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

class AuthViewModel : ViewModel() {
    var loginEmail by mutableStateOf("")
    var loginPassword by mutableStateOf("")

    var regName by mutableStateOf("")
    var regEmail by mutableStateOf("")
    var regPassword by mutableStateOf("")

    var isLoading by mutableStateOf(false)

    fun signIn(onSuccess: () -> Unit) {
        isLoading = true
        // В будущем здесь будет вызов Retrofit
        onSuccess()
        isLoading = false
    }

    fun signUp(onSuccess: () -> Unit) {
        isLoading = true
        // В будущем здесь будет вызов Retrofit
        onSuccess()
        isLoading = false
    }
}