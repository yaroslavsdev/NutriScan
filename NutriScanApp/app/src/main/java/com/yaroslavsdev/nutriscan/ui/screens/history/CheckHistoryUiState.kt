package com.yaroslavsdev.nutriscan.ui.screens.history

import com.yaroslavsdev.nutriscan.ui.model.ScannedProductUi
import java.time.LocalDate

data class CheckHistoryUiState(
    val grouped: Map<LocalDate, List<ScannedProductUi>> = emptyMap(),
    val isEmpty: Boolean = true
)