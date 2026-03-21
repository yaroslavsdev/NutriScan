package com.yaroslavsdev.nutriscan.ui.screens.auth

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yaroslavsdev.nutriscan.data.repository.AuthRepository
import kotlinx.coroutines.launch
import org.json.JSONObject
import retrofit2.HttpException

class AuthViewModel(private val repository: AuthRepository) : ViewModel() {
    // Поля ввода
    var loginEmail by mutableStateOf("")
    var loginPassword by mutableStateOf("")
    var regName by mutableStateOf("")
    var regEmail by mutableStateOf("")
    var regPassword by mutableStateOf("")

    // Ошибки валидации
    var emailError by mutableStateOf<String?>(null)
    var passwordError by mutableStateOf<String?>(null)
    var regNameError by mutableStateOf<String?>(null)

    var isLoading by mutableStateOf(false)
    var serverError by mutableStateOf<String?>(null)

    // Фильтрация ввода
    fun updateLoginEmail(input: String) {
        loginEmail = input.replace("\n", "").replace(" ", "").trim()
        emailError = null
        serverError = null
    }

    fun updateLoginPassword(input: String) {
        loginPassword = input.replace("\n", "").replace(" ", "").trim()
        passwordError = null
        serverError = null
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

    fun signIn(onSuccess: () -> Unit) {
        if (!validateSignIn()) return

        viewModelScope.launch {
            isLoading = true
            serverError = null

            val result = repository.login(loginEmail, loginPassword)

            isLoading = false
            result.onSuccess {
                onSuccess()
            }
            result.onFailure { exception ->
                serverError = mapErrorToMessage(exception)
            }
        }
    }

    fun signUp(onSuccess: () -> Unit) {
        if (!validateSignUp()) return

        viewModelScope.launch {
            isLoading = true
            serverError = null

            val result = repository.register(regName, regEmail, regPassword)

            isLoading = false
            result.onSuccess {
                onSuccess()
            }
            result.onFailure { exception ->
                serverError = mapErrorToMessage(exception)
            }
        }
    }

    private fun mapErrorToMessage(throwable: Throwable): String {
        return when (throwable) {
            is HttpException -> {
                try {
                    val errorBody = throwable.response()?.errorBody()?.string()
                    val json = JSONObject(errorBody ?: "")
                    json.getString("detail")
                } catch (e: Exception) {
                    when (throwable.code()) {
                        401 -> "Неверный логин или пароль"
                        404 -> "Аккаунт не найден"
                        500 -> "Ошибка на сервере, скоро починим"
                        else -> "Ошибка сервера (${throwable.code()})"
                    }
                }
            }
            is java.net.UnknownHostException, is java.net.ConnectException -> {
                "Нет связи с сервером"
            }
            is java.net.SocketTimeoutException -> {
                "Сервер слишком долго не отвечает"
            }
            else -> "Что-то пошло не так, попробуйте позже"
        }
    }
}