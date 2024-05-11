package edu.agh.susgame.front.ui.component.game.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import edu.agh.susgame.front.model.game.GameId
import edu.agh.susgame.front.providers.interfaces.ServerMapProvider
import edu.agh.susgame.front.ui.component.game.computer.ComputerComponent
import edu.agh.susgame.front.ui.component.game.map.ServerMapView

@Composable
fun GameNavigationHost(
    gameId: GameId?,
    padding: PaddingValues,
    navController: NavHostController,
    serverMapProvider: ServerMapProvider,
) {
    NavHost(
        navController = navController,
        startDestination = GameRoute.Map.route,
        modifier = Modifier.padding(padding)
    ) {
        composable(GameRoute.Map.route) {
            ServerMapView(gameId, serverMapProvider)
        }
        composable(GameRoute.Computer.route) {
            ComputerComponent()
        }
    }
}