import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.yaroslavsdev.nutriscan.ui.navigation.MainContentScreen
import com.yaroslavsdev.nutriscan.ui.navigation.Screen
import com.yaroslavsdev.nutriscan.ui.screens.auth.AuthScreen

@Composable
fun AppNavigation(navController: NavHostController, modifier: Modifier = Modifier) {
    NavHost(
        navController = navController,
        startDestination = Screen.Auth.route,
        modifier = modifier
    ) {
        composable(Screen.Auth.route) {
            AuthScreen(navController)
        }
        composable(Screen.Main.route) {
            MainContentScreen()
        }
    }
}