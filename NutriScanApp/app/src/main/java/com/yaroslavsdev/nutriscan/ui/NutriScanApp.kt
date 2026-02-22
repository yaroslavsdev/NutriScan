package com.yaroslavsdev.nutriscan.ui

import AppNavigation
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.yaroslavsdev.nutriscan.ui.navigation.BottomNavItem
import com.yaroslavsdev.nutriscan.ui.components.BottomBar
import kotlin.collections.contains

@Composable
fun NutriScanApp() {
    val navController = rememberNavController()

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    val routesWithBottomBar = listOf(
        BottomNavItem.Home.route,
        BottomNavItem.Diary.route,
        BottomNavItem.History.route,
        BottomNavItem.Profile.route
    )

    val showBottomBar = currentRoute in routesWithBottomBar

    Scaffold(
        bottomBar = {
            if (showBottomBar) {
                BottomBar(navController)
            }
        }
    ) { innerPadding ->
        AppNavigation(
            navController = navController,
            modifier = Modifier.padding(innerPadding)
        )
    }
}