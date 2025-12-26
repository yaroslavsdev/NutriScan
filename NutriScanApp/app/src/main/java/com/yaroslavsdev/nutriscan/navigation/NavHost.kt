import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.yaroslavsdev.nutriscan.navigation.BottomNavItem
import com.yaroslavsdev.nutriscan.ui.screens.*

@Composable
fun AppNavigation(navController: NavHostController, modifier: Modifier = Modifier) {
    NavHost(
        navController = navController,
        startDestination = BottomNavItem.Home.route,
        modifier = modifier
    ) {
        composable(BottomNavItem.Home.route) { HomeScreen(navController) }
        composable(BottomNavItem.Diary.route) { FoodDiaryScreen(navController) }
        composable(BottomNavItem.History.route) { CheckHistoryScreen(navController) }
        composable(BottomNavItem.Profile.route) { ProfileScreen(navController) }

        composable("scanner") { ScannerScreen(navController) }
    }
}