package edu.agh.susgame.front.ui.component.game.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MailOutline
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
                label = "Map",
                icon = Icons.Filled.MailOutline,
                route = GameRoute.Map.route
            ),
            BottomNavigationItem(
                label = "Computer",
                icon = Icons.Filled.Settings,
                route = GameRoute.Computer.route
            ),
        )
    }
}
