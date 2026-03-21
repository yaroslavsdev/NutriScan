package com.yaroslavsdev.nutriscan.ui.navigation
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.yaroslavsdev.nutriscan.data.local.TokenManager
import com.yaroslavsdev.nutriscan.ui.screens.auth.AuthScreen

@Composable
fun AppNavigation(navController: NavHostController) {
    val context = LocalContext.current
    val tokenManager = remember { TokenManager(context) }

    val startRoute = if (tokenManager.getToken() != null) {
        Screen.Main.route
    } else {
        Screen.Auth.route
    }

    NavHost(
        navController = navController,
        startDestination = startRoute
    ) {
        composable(Screen.Auth.route) {
            AuthScreen(navController)
        }
        composable(Screen.Main.route) {
            MainContentScreen()
        }
    }
}