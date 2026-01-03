package com.yaroslavsdev.nutriscan.ui.screens.scan

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun PermissionDeniedScreen(
    onRequestPermission: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize().padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Для работы сканера нужен доступ к камере")

        Spacer(Modifier.height(16.dp))

        Button(onClick = onRequestPermission) {
            Text("Разрешить")
        }
    }
}
