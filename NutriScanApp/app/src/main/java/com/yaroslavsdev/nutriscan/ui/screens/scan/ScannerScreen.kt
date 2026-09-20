package com.yaroslavsdev.nutriscan.ui.screens.scan

import android.Manifest
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.yaroslavsdev.nutriscan.ui.navigation.Screen
import org.koin.androidx.compose.koinViewModel

@Composable
fun ScannerScreen(
    navController: NavController,
    onBack: () -> Unit,
    viewModel: ScanViewModel = koinViewModel()
) {
    val state by viewModel.state.collectAsState()

    val permissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { granted ->
        viewModel.onPermissionResult(granted)
    }

    LaunchedEffect(Unit) {
        permissionLauncher.launch(Manifest.permission.CAMERA)
    }

    when {
        !state.hasPermission -> {
            PermissionDeniedScreen {
                permissionLauncher.launch(Manifest.permission.CAMERA)
            }
        }

        state.barcode != null -> {
            LaunchedEffect(state.barcode) {
                navController.navigate(Screen.ProductScreen.createRoute(state.barcode!!))
                viewModel.reset()
            }
        }

        else -> {
            Column(
                modifier = Modifier
                    .fillMaxSize(),
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, null)
                    }
                    Text("Сканирование штрих-кода", style = MaterialTheme.typography.titleLarge)
                }

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                        .padding(horizontal = 16.dp, vertical = 16.dp)
                ) {
                    CameraPreview(viewModel)
                }
            }
        }
    }
}