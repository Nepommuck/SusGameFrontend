package edu.agh.susgame.front.ui.component.game.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import edu.agh.susgame.front.model.game.LobbyId
import edu.agh.susgame.front.navigation.GameRoute
import edu.agh.susgame.front.navigation.MenuRoute
import edu.agh.susgame.front.providers.interfaces.ServerMapProvider
import edu.agh.susgame.front.ui.Translation
import edu.agh.susgame.front.ui.component.game.computer.ComputerComponent
import edu.agh.susgame.front.ui.component.game.map.ServerMapView

@Composable
fun GameNavigationHostComponent(
    lobbyId: LobbyId?,
    padding: PaddingValues,
    menuNavController: NavHostController,
    gameNavController: NavHostController,
    serverMapProvider: ServerMapProvider,
) {
    when (lobbyId) {
        null -> {
            Text(text = Translation.Error.UnexpectedError)
            Button(onClick = {
                menuNavController.navigate(MenuRoute.MainMenu.route)
            }) {
                Text(text = Translation.Button.BACK_TO_MAIN_MENU)
            }
        }

        else -> NavHost(
            navController = gameNavController,
            startDestination = GameRoute.Map.route,
            modifier = Modifier.padding(padding),
        ) {
            composable(GameRoute.Map.route) {
                ServerMapView(lobbyId, serverMapProvider, menuNavController)
            }
            composable(GameRoute.Computer.route) {
                ComputerComponent()
            }
        }
    }
}
