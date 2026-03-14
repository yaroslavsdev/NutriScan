package com.yaroslavsdev.nutriscan.ui.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.yaroslavsdev.nutriscan.ui.components.BottomBar
import com.yaroslavsdev.nutriscan.ui.screens.HomeScreen
import com.yaroslavsdev.nutriscan.ui.screens.diary.FoodDiaryScreen
import com.yaroslavsdev.nutriscan.ui.screens.history.CheckHistoryScreen
import com.yaroslavsdev.nutriscan.ui.screens.product.ProductScreen
import com.yaroslavsdev.nutriscan.ui.screens.profile.ProfileScreen
import com.yaroslavsdev.nutriscan.ui.screens.scan.ScannerScreen

@Composable
fun MainContentScreen() {
    val bottomNavController = rememberNavController()

    Scaffold(
        bottomBar = {
            BottomBar(navController = bottomNavController)
        }
    ) { innerPadding ->
        NavHost(
            navController = bottomNavController,
            startDestination = BottomNavItem.Home.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(BottomNavItem.Home.route) { HomeScreen(bottomNavController) }
            composable(BottomNavItem.Diary.route) { FoodDiaryScreen(bottomNavController) }
            composable(BottomNavItem.History.route) { CheckHistoryScreen(bottomNavController) }
            composable(BottomNavItem.Profile.route) { ProfileScreen(bottomNavController) }

            composable("scanner") { ScannerScreen(bottomNavController) }
            composable(
                route = "product/{barcode}",
                arguments = listOf(navArgument("barcode") { type = NavType.StringType })
            ) { backStackEntry ->
                val barcode = backStackEntry.arguments?.getString("barcode") ?: ""
                ProductScreen(barcode = barcode, onBack = { bottomNavController.popBackStack() })
            }
        }
    }
}