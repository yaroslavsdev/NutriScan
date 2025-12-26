package com.yaroslavsdev.nutriscan.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.ui.graphics.vector.ImageVector
import com.yaroslavsdev.nutriscan.domain.model.MenuTab

sealed class BottomNavItem(
    route: String,
    label: String,
    val icon: ImageVector
) : MenuTab(route, label) {
    object Home: BottomNavItem("home", "Главная", Icons.Default.Home)
    object Diary: BottomNavItem("diary", "Дневник", Icons.Default.DateRange)
    object History: BottomNavItem("history", "История", Icons.Default.Refresh)
    object Profile: BottomNavItem("profile", "Профиль", Icons.Default.Person,)
}