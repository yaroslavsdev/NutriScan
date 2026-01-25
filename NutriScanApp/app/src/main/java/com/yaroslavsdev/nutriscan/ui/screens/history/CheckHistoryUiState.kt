package com.yaroslavsdev.nutriscan.ui.screens.history

import com.yaroslavsdev.nutriscan.domain.model.ScanHistoryItem
import java.time.LocalDate

data class CheckHistoryUiState(
    val grouped: Map<LocalDate, List<ScanHistoryItem>> = emptyMap(),
    val isEmpty: Boolean = true
)