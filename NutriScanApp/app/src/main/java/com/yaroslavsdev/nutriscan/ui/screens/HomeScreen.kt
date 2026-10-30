package com.yaroslavsdev.nutriscan.ui.screens


import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.yaroslavsdev.nutriscan.ui.navigation.Screen
import com.yaroslavsdev.nutriscan.ui.screens.scan.ManualBarcodeDialog

@Composable
fun HomeScreen(navController: NavHostController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 12.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        var onScanClicked = false
        var showManualDialog by remember { mutableStateOf(false) }

        Text("Проверьте продукт, нажав кнопку")

        Button(
            onClick = {
                if (!onScanClicked) {
                    onScanClicked = true
                    navController.navigate(Screen.ScannerScreen.route)
                } },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            shape = MaterialTheme.shapes.large
        ) {
            Text("Сканировать товар")
        }

        Button(
            onClick = { showManualDialog = true },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            shape = MaterialTheme.shapes.large
        ) {
            Text("Ввести штрих-код вручную")
        }

        if (showManualDialog) {
            ManualBarcodeDialog(
                onDismiss = { showManualDialog = false },
                onConfirm = { code ->
                    showManualDialog = false
                    navController.navigate(Screen.ProductScreen.createRoute(code))
                }
            )
        }
    }
}