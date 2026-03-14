import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.yaroslavsdev.nutriscan.ui.navigation.BottomNavItem
import com.yaroslavsdev.nutriscan.ui.screens.*
import com.yaroslavsdev.nutriscan.ui.screens.diary.FoodDiaryScreen
import com.yaroslavsdev.nutriscan.ui.screens.history.CheckHistoryScreen
import com.yaroslavsdev.nutriscan.ui.screens.product.ProductScreen
import com.yaroslavsdev.nutriscan.ui.screens.profile.ProfileScreen
import com.yaroslavsdev.nutriscan.ui.screens.scan.ScannerScreen

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

        composable(
            route = "product/{barcode}",
            arguments = listOf(
                navArgument("barcode") {
                    type = NavType.StringType
                }
            )
        ) { backStackEntry ->

            val barcode = backStackEntry.arguments?.getString("barcode") ?: ""

            ProductScreen(
                barcode = barcode,
                onBack = { navController.popBackStack() }
            )
        }
    }
}