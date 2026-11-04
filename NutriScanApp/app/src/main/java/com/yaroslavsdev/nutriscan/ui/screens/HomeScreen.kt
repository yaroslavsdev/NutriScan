package com.yaroslavsdev.nutriscan.ui.screens


import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.Edit
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.navigation.NavHostController
import com.yaroslavsdev.nutriscan.R
import com.yaroslavsdev.nutriscan.ui.navigation.Screen
import com.yaroslavsdev.nutriscan.ui.screens.scan.ManualBarcodeDialog

@Composable
fun HomeScreen(navController: NavHostController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 20.dp, vertical = 8.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        fun safeNavigate(route: String) {
            val entry = navController.currentBackStackEntry
            if (entry?.lifecycle?.currentState == Lifecycle.State.RESUMED) {
                navController.navigate(route)
            }
        }

        var showManualDialog by remember { mutableStateOf(false) }

        Text(
            text = "Проверка продукта",
            style = MaterialTheme.typography.headlineMedium,
            textAlign = TextAlign.Center
        )

        Text(
            text = "Отсканируйте штрих-код или введите его вручную",
            style = MaterialTheme.typography.bodyMedium,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(top = 8.dp, bottom = 32.dp)
        )

        Button(
            onClick = { safeNavigate(Screen.ScannerScreen.route) },
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            shape = MaterialTheme.shapes.large
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_camera),
                contentDescription = "Сканировать товар",
                modifier = Modifier.padding(end = 8.dp).size(20.dp)
            )
            Text("Сканировать товар", style = MaterialTheme.typography.titleMedium)
        }

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedButton(
            onClick = { showManualDialog = true },
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            shape = MaterialTheme.shapes.large
        ) {
            Icon(
                imageVector = Icons.Rounded.Edit,
                contentDescription = "Ручной ввод кода",
                modifier = Modifier.padding(end = 8.dp).size(20.dp)
            )
            Text("Ввести код вручную", style = MaterialTheme.typography.titleMedium)
        }

        Spacer(modifier = Modifier.height(16.dp))

        TextButton(
            onClick = { safeNavigate(Screen.AddCustomProductScreen.route) },
            modifier = Modifier.fillMaxWidth(),
            shape = MaterialTheme.shapes.large
        ) {
            Icon(
                imageVector = Icons.Rounded.Add,
                contentDescription = null,
                modifier = Modifier.padding(end = 8.dp).size(20.dp)
            )
            Text("Товар без штрих-кода")
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