package com.yaroslavsdev.nutriscan.ui.screens


import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.yaroslavsdev.nutriscan.ui.theme.NutriScanTheme

@Composable
fun HomeScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Здравствуйте, уважаемый пользователь")

        Spacer(Modifier.padding(16.dp))

        Button(
            onClick = {}
        ) {
            Text("Сканировать товар")
        }
    }
}


@Preview(showBackground = true)
@Composable
fun HomePreview() {
    NutriScanTheme {
        HomeScreen()
    }
}