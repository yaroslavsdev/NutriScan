package com.yaroslavsdev.nutriscan.ui.screens.scan

import android.Manifest
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.ImageCapture
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
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
    ) { provided ->
        viewModel.onPermissionResult(provided)
    }

    LaunchedEffect(Unit) {
        permissionLauncher.launch(Manifest.permission.CAMERA)
    }

    when {
        !state.hasPermission -> {
            PermissionDeniedScreen({ permissionLauncher.launch(Manifest.permission.CAMERA) })
        }

        state.barcode != null -> {
            LaunchedEffect(state.barcode) {
                navController.navigate(Screen.ProductScreen.createRoute(state.barcode!!))
                viewModel.reset()
            }
        }

        else -> {
            val context = LocalContext.current
            var imageCapture by remember { mutableStateOf<ImageCapture?>(null) }
            var isProcessing by remember { mutableStateOf(false) }

            Column(
                modifier = Modifier.fillMaxSize()
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, "Кнопка назад")
                    }
                    Text("Сканирование штрих-кода", style = MaterialTheme.typography.titleMedium)
                }

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                        .padding(horizontal = 12.dp)
                ) {
                    CameraPreview(onImageCaptureReady = { imageCapture = it })
                }

                Button(
                    onClick = {
                        val capture = imageCapture ?: return@Button
                        isProcessing = true

                        takePhotoAndScan(context, capture) { result ->
                            isProcessing = false
                            when (result) {
                                is ScanResult.Success -> {
                                    viewModel.onBarcodeDetected(result.barcode)
                                }
                                ScanResult.InvalidCheckSum -> {
                                    Toast.makeText(context, "Неверная контрольная сумма штрих-кода", Toast.LENGTH_SHORT).show()
                                }
                                ScanResult.NoBarcodeFound -> {
                                    Toast.makeText(context, "Штрих-код не обнаружен", Toast.LENGTH_SHORT).show()
                                }
                                is ScanResult.Failure -> {
                                    Toast.makeText(context, "Ошибка камеры или сканера", Toast.LENGTH_SHORT).show()
                                }
                            }
                        }
                    },
                    enabled = imageCapture != null && !isProcessing,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 12.dp, vertical = 8.dp),
                    shape = MaterialTheme.shapes.large
                ) {
                    Text(if (isProcessing) "Обработка..." else "Сделать фото")
                }
            }
        }
    }
}