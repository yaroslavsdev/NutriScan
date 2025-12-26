package com.yaroslavsdev.nutriscan

import AppNavigation
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.yaroslavsdev.nutriscan.navigation.BottomNavItem
import com.yaroslavsdev.nutriscan.ui.components.BottomBar
import com.yaroslavsdev.nutriscan.ui.theme.NutriScanTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            NutriScanTheme {
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
        }
    }
}