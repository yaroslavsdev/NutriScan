package com.yaroslavsdev.nutriscan.ui.screens.scan

import android.Manifest
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@Composable
fun ScannerScreen(
    navController: NavController,
    viewModel: ScanViewModel = androidx.lifecycle.viewmodel.compose.viewModel()
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
            ScanResultScreen(
                barcode = state.barcode!!,
                onScanAgain = { viewModel.reset() }
            )
        }

        else -> {
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Сканирование штрих-кода",
                    modifier = Modifier.padding(16.dp)
                )

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(1f)
                        .padding(16.dp)
                ) {
                    CameraPreview(viewModel)
                }
            }
        }
    }
}
