package com.yaroslavsdev.nutriscan.ui.navigation
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.yaroslavsdev.nutriscan.data.local.TokenManager
import com.yaroslavsdev.nutriscan.ui.screens.allergens.AllergensScreen
import com.yaroslavsdev.nutriscan.ui.screens.auth.AuthScreen
import org.koin.compose.koinInject

@Composable
fun AppNavigation(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    val tokenManager: TokenManager = koinInject()

    val startRoute = remember {
        if (tokenManager.getToken() != null) {
            Screen.Main.route
        } else {
            Screen.Auth.route
        }
    }

    NavHost(
        navController = navController,
        startDestination = startRoute,
        modifier = modifier
    ) {
        composable(Screen.Auth.route) {
            AuthScreen(navController)
        }

        composable(
            route = "allergens_screen/{fromRegistration}",
            arguments = listOf(navArgument("fromRegistration") { type = NavType.BoolType })
        ) { backStackEntry ->
            val fromRegistration = backStackEntry.arguments?.getBoolean("fromRegistration") ?: false

            AllergensScreen(
                navController = navController,
                fromRegistration = fromRegistration
            )
        }

        composable(Screen.Main.route) {
            MainContentScreen(rootNavController = navController)
        }
    }
}