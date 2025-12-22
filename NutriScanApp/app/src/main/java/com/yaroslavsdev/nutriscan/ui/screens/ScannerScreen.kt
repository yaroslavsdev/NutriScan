package com.yaroslavsdev.nutriscan.ui.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.yaroslavsdev.nutriscan.ui.theme.NutriScanTheme

@Composable
fun ScannerScreen(navController: NavHostController) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text("Здесь будет сканер")
    }
}



@Preview(showBackground = true)
@Composable
fun ScannerPreview() {
    NutriScanTheme {
        HomeScreen(navController = rememberNavController())
    }
}