package com.yaroslavsdev.nutriscan.ui.screens.auth

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

class AuthViewModel : ViewModel() {
    // Поля входа
    var loginEmail by mutableStateOf("")
    var loginPassword by mutableStateOf("")

    // Поля регистрации
    var regName by mutableStateOf("")
    var regEmail by mutableStateOf("")
    var regPassword by mutableStateOf("")

    // Ошибки
    var emailError by mutableStateOf<String?>(null)
    var passwordError by mutableStateOf<String?>(null)
    var regNameError by mutableStateOf<String?>(null)

    var isLoading by mutableStateOf(false)

    // Фильтрация ввода
    fun updateLoginEmail(input: String) {
        loginEmail = input.replace("\n", "").replace(" ", "")
        emailError = null
    }

    fun updateLoginPassword(input: String) {
        loginPassword = input.replace("\n", "").replace(" ", "")
        passwordError = null
    }

    fun updateRegName(input: String) {
        regName = input.replace("\n", "").replace(" ", "")
        regNameError = null
    }

    fun updateRegEmail(input: String) {
        regEmail = input.replace("\n", "").replace(" ", "")
        emailError = null
    }

    fun updateRegPassword(input: String) {
        regPassword = input.replace("\n", "").replace(" ", "")
        passwordError = null
    }

    fun validateSignIn(): Boolean {
        var isValid = true
        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(loginEmail).matches()) {
            emailError = "Введите корректный Email"
            isValid = false
        }
        if (loginPassword.length < 6) {
            passwordError = "Минимум 6 символов"
            isValid = false
        }
        return isValid
    }

    fun validateSignUp(): Boolean {
        var isValid = true
        if (regName.isBlank()) { regNameError = "Введите имя"; isValid = false }
        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(regEmail).matches()) { emailError = "Некорректный Email"; isValid = false }
        if (regPassword.length < 6) { passwordError = "Минимум 6 символов"; isValid = false }
        return isValid
    }

    fun signIn(onSuccess: () -> Unit) { /* Будущий Retrofit */ onSuccess() }
    fun signUp(onSuccess: () -> Unit) { /* Будущий Retrofit */ onSuccess() }
}