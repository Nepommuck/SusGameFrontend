package edu.agh.susgame.front.ui.component.menu.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Settings
import androidx.compose.ui.graphics.vector.ImageVector

data class BottomNavigationItem(
    val label: String,
    val icon: ImageVector,
    val route: String,
) {
    companion object {
        val allMenuItems = listOf(
            BottomNavigationItem(
                label = "Profile",
                icon = Icons.Filled.AccountCircle,
                route = MenuRoute.Profile.route
            ),
            BottomNavigationItem(
                label = "Play",
                icon = Icons.Filled.PlayArrow,
                route = MenuRoute.FindGame.route
            ),
            BottomNavigationItem(
                label = "Settings",
                icon = Icons.Filled.Settings,
                route = MenuRoute.Settings.route
            ),
        )
    }
}


