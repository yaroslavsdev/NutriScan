package com.yaroslavsdev.nutriscan.ui.navigation

sealed class Screen(val route: String) {
    object Auth : Screen("auth_root")
    object Main : Screen("main_root")
}