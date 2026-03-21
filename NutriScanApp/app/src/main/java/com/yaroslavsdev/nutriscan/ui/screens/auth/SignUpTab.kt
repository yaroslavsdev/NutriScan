package com.yaroslavsdev.nutriscan.ui.screens.auth

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.yaroslavsdev.nutriscan.ui.navigation.Screen

@Composable
fun SignUpTab(navController: NavController, viewModel: AuthViewModel) {
    Column(
        modifier = Modifier.fillMaxSize().padding(24.dp).verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("Создать профиль", style = MaterialTheme.typography.headlineMedium)
        Spacer(Modifier.height(14.dp))

        OutlinedTextField(
            value = viewModel.regName,
            onValueChange = { viewModel.updateRegName(it) },
            label = { Text("Имя") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            isError = viewModel.regNameError != null,
            supportingText = { viewModel.regNameError?.let { Text(it) } },
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next)
        )

        Spacer(Modifier.height(4.dp))

        OutlinedTextField(
            value = viewModel.regEmail,
            onValueChange = { viewModel.updateRegEmail(it) },
            label = { Text("Email") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            isError = viewModel.emailError != null,
            supportingText = { viewModel.emailError?.let { Text(it) } },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email, imeAction = ImeAction.Next)
        )

        Spacer(Modifier.height(4.dp))

        OutlinedTextField(
            value = viewModel.regPassword,
            onValueChange = { viewModel.updateRegPassword(it) },
            label = { Text("Пароль") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            visualTransformation = PasswordVisualTransformation(),
            isError = viewModel.passwordError != null,
            supportingText = { viewModel.passwordError?.let { Text(it) } },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password, imeAction = ImeAction.Done)
        )

        Spacer(Modifier.height(24.dp))

        Button(
            onClick = {
                if (viewModel.validateSignUp()) {
                    viewModel.signUp {
                        navController.navigate(Screen.Main.route) {
                            popUpTo(Screen.Auth.route) { inclusive = true }
                        }
                    }
                }
            },
            modifier = Modifier.fillMaxWidth(),
            shape = MaterialTheme.shapes.medium
        ) {
            Text("Зарегистрироваться")
        }

        viewModel.serverError?.let { error ->
            Text(
                text = error,
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.padding(vertical = 8.dp)
            )
        }
    }
}