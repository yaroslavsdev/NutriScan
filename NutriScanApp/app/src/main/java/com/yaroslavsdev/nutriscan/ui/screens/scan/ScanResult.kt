package com.yaroslavsdev.nutriscan.ui.screens.scan

sealed interface ScanResult {
    data class Success(val barcode: String) : ScanResult
    object NoBarcodeFound : ScanResult
    object InvalidCheckSum : ScanResult
    data class Failure(val exception: Throwable) : ScanResult
}