package com.yaroslavsdev.nutriscan.ui.screens.history

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.yaroslavsdev.nutriscan.domain.model.ScanHistoryItem
import java.time.format.DateTimeFormatter

@Composable
fun HistoryCard(item: ScanHistoryItem) {
    Card(
        modifier = Modifier
            .fillMaxSize()
            .padding(vertical = 4.dp)
    ) {
        Column(Modifier.padding(12.dp)) {
            Text("Штрих-код: ${item.productBarcode}")

            Spacer(Modifier.height(4.dp))

            Text(
                item.scannedAt.format(DateTimeFormatter.ofPattern("HH:mm"))
            )
        }
    }
}