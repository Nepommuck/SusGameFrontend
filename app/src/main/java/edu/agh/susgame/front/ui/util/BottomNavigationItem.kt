package edu.agh.susgame.front.ui.util

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MailOutline
import androidx.compose.material.icons.filled.Settings
import androidx.compose.ui.graphics.vector.ImageVector
import edu.agh.susgame.front.navigation.GameRoute
import edu.agh.susgame.front.ui.Translation

data class BottomNavigationItem(
    val label: String,
    val icon: ImageVector,
    val route: String,
) {
    companion object {
        val allMenuItems = listOf(
            BottomNavigationItem(
                label = Translation.Game.MAP,
                icon = Icons.Filled.MailOutline,
                route = GameRoute.Map.route
            ),
            BottomNavigationItem(
                label = Translation.Game.COMPUTER,
                icon = Icons.Filled.Settings,
                route = GameRoute.Computer.route
            ),
        )
    }
}
