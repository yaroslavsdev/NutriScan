package com.yaroslavsdev.nutriscan.ui.screens.scan

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun ScanResultScreen(
    barcode: String,
    onScanAgain: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Штрих-код найден:")
        Spacer(Modifier.height(8.dp))
        Text(barcode)
        Spacer(Modifier.height(24.dp))
        Button(onClick = onScanAgain) {
            Text("Сканировать ещё")
        }
    }
}
