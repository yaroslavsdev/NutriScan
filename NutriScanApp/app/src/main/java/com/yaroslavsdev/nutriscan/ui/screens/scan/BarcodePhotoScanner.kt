package com.yaroslavsdev.nutriscan.ui.screens.scan

import android.content.Context
import androidx.annotation.OptIn
import androidx.camera.core.ExperimentalGetImage
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.core.content.ContextCompat
import com.google.mlkit.vision.barcode.BarcodeScannerOptions
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.barcode.common.Barcode
import com.google.mlkit.vision.common.InputImage
import com.yaroslavsdev.nutriscan.domain.usecase.BarcodeValidator.isValidBarcodeSum

private val options = BarcodeScannerOptions.Builder()
    .setBarcodeFormats(Barcode.FORMAT_EAN_8, Barcode.FORMAT_EAN_13)
    .build()

private val scanner = BarcodeScanning.getClient(options)

fun takePhotoAndScan(
    context: Context,
    imageCapture: ImageCapture,
    onResult: (ScanResult) -> Unit
) {
    imageCapture.takePicture(
        ContextCompat.getMainExecutor(context),
        object : ImageCapture.OnImageCapturedCallback() {

            @OptIn(ExperimentalGetImage::class)
            override fun onCaptureSuccess(imageProxy: androidx.camera.core.ImageProxy) {
                try {
                    val mediaImage = imageProxy.image
                    if (mediaImage != null) {
                        val image = InputImage.fromMediaImage(
                            mediaImage,
                            imageProxy.imageInfo.rotationDegrees
                        )

                        scanner.process(image)
                            .addOnSuccessListener { barcodes ->
                                val barcode = barcodes.firstOrNull()?.rawValue
                                when {
                                    barcode == null -> {
                                        onResult(ScanResult.NoBarcodeFound)
                                    }
                                    isValidBarcodeSum(barcode) -> {
                                        onResult(ScanResult.Success(barcode))
                                    }
                                    else -> {
                                        onResult(ScanResult.InvalidCheckSum)
                                    }
                                }
                            }
                            .addOnFailureListener { e ->
                                onResult(ScanResult.Failure(e))
                            }
                            .addOnCompleteListener {
                                imageProxy.close()
                            }
                    } else {
                        imageProxy.close()
                        onResult(ScanResult.NoBarcodeFound)
                    }
                } catch (e: Exception) {
                    imageProxy.close()
                    onResult(ScanResult.Failure(e))
                }
            }

            override fun onError(exception: ImageCaptureException) {
                onResult(ScanResult.Failure(exception))
            }
        }
    )
}