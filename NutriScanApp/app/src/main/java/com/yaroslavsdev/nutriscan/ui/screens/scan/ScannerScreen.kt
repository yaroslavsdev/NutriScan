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
import com.yaroslavsdev.nutriscan.ui.navigation.Screen
import org.koin.androidx.compose.koinViewModel

@Composable
fun ScannerScreen(
    navController: NavController,
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
                    .statusBarsPadding()
                    .padding(horizontal = 8.dp, vertical = 10.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Сканирование штрих-кода",
                    modifier = Modifier.padding(top = 30.dp, end = 22.dp)
                )

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                        .padding(horizontal = 16.dp, vertical = 24.dp)
                ) {
                    CameraPreview(viewModel)
                }
            }
        }
    }
}
