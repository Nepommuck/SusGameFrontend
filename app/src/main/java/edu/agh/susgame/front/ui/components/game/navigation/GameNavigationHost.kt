package edu.agh.susgame.front.ui.components.game.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import edu.agh.susgame.dto.rest.model.LobbyId
import edu.agh.susgame.front.navigation.GameRoute
import edu.agh.susgame.front.navigation.MenuRoute
import edu.agh.susgame.front.service.interfaces.GameService
import edu.agh.susgame.front.service.interfaces.ServerMapProvider
import edu.agh.susgame.front.Translation
import edu.agh.susgame.front.ui.components.game.components.computer.ComputerComponent
import edu.agh.susgame.front.ui.components.game.components.map.GameView

@Composable
fun GameNavigationHost(
    lobbyId: LobbyId?,
    padding: PaddingValues,
    menuNavController: NavHostController,
    gameNavController: NavHostController,
    serverMapProvider: ServerMapProvider,
    gameService: GameService,
) {
    when (lobbyId) {
        null -> {
            Text(text = Translation.Error.UNEXPECTED_ERROR)
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
                GameView(lobbyId, serverMapProvider, menuNavController, gameNavController)
            }
            composable(GameRoute.Computer.route) {
                ComputerComponent(gameService,gameNavController)
            }
        }
    }
}
