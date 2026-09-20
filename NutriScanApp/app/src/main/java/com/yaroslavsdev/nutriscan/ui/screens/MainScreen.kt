package com.yaroslavsdev.nutriscan.ui.screens


import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.yaroslavsdev.nutriscan.ui.navigation.Screen

@Composable
fun HomeScreen(navController: NavHostController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 12.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        var onButtonClicked = false

        Spacer(Modifier.padding(vertical = 16.dp))

        Text("Проверьте продукт, нажав кнопку")

        Spacer(Modifier.padding(vertical = 16.dp))

        Button(
            onClick = {
                if (!onButtonClicked) {
                    onButtonClicked = true
                    navController.navigate(Screen.ScannerScreen.route)
                } },
            shape = MaterialTheme.shapes.large
        ) {
            Text("Сканировать товар")
        }
    }
}