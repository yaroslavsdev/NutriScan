package com.yaroslavsdev.nutriscan.domain.model

import java.time.LocalDateTime

data class ScanHistoryItem (
    val productBarcode: Int,
    val scannedAt: LocalDateTime
)