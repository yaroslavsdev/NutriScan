package com.yaroslavsdev.nutriscan.ui.navigation

sealed class Screen(
    val route: String
) {
    object Auth : Screen("auth_root")
    object Main : Screen("main_root")
    object NutritionScreen : Screen("nutrition_screen")
    object ScannerScreen : Screen("scanner_screen")
    object EditNameScreen : Screen("edit_name_screen")
    object ChangePasswordScreen : Screen("change_password_screen")

    object NutritionStatsScreen : Screen("nutrition_stats_screen/{proteins}/{fats}/{carbs}/{totalWeight}") {
        fun createRoute(proteins: Float, fats: Float, carbs: Float, totalWeight: Float): String {
            return "nutrition_stats_screen/$proteins/$fats/$carbs/$totalWeight"
        }
    }

    object AllergensScreen : Screen("allergens_screen/{fromRegistration}") {
        fun createRoute(fromRegistration: Boolean): String {
            return "allergens_screen/$fromRegistration"
        }
    }

    object ProductScreen : Screen("product_screen/{barcode}") {
        fun createRoute(barcode: String): String {
            return "product_screen/$barcode"
        }
    }

    object AddProductScreen : Screen("addProductScreen/{barcode}") {
        fun createRoute(barcode: String): String {
            return "addProductScreen/$barcode"
        }
    }
}