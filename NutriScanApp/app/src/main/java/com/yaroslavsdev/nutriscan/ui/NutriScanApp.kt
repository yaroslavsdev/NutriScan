package com.yaroslavsdev.nutriscan.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.yaroslavsdev.nutriscan.ui.navigation.AppNavigation

@Composable
fun NutriScanApp() {
    val navController = rememberNavController()

    AppNavigation(
        navController = navController,
        modifier = Modifier
    )
}