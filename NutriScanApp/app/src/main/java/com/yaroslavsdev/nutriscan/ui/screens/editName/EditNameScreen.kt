package com.yaroslavsdev.nutriscan.ui.screens.editName

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import org.koin.androidx.compose.koinViewModel

@Composable
fun EditNameScreen(
    navController: NavController,
    viewModel: EditNameViewModel = koinViewModel()
) {
    val context = LocalContext.current
    val currentUsername by viewModel.username.collectAsState()
    val isSaving by viewModel.isSaving.collectAsState()
    val isError by viewModel.isError.collectAsState()

    LaunchedEffect(isError) {
        if (isError) {
            Toast.makeText(context, "Введите корректное имя", Toast.LENGTH_SHORT).show()
            viewModel.consumeError()
        }
    }

    var username by remember { mutableStateOf("") }

    LaunchedEffect(currentUsername) {
        username = currentUsername
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            IconButton(onClick = { navController.popBackStack() }) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, null)
            }
            Text("Изменить имя", style = MaterialTheme.typography.titleLarge)
        }

        Spacer(Modifier.height(24.dp))

        OutlinedTextField(
            value = username,
            onValueChange = { username = it },
            label = { Text("Имя") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.height(24.dp))

        Button(
            onClick = {
                viewModel.saveUsername(username) {
                    navController.popBackStack()
                }
            },
            enabled = !isSaving,
            modifier = Modifier.fillMaxWidth(),
            shape = MaterialTheme.shapes.large
        ) {
            Text("Сохранить")
        }
    }
}