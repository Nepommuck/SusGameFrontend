package edu.agh.susgame.front.gui.components.menu.components.createlobby.elements

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavController
import edu.agh.susgame.front.gui.components.common.theme.PaddingL
import edu.agh.susgame.front.gui.components.common.util.Translation
import edu.agh.susgame.front.gui.components.menu.navigation.MenuRoute
import edu.agh.susgame.front.service.interfaces.CreateNewGameResult
import edu.agh.susgame.front.service.interfaces.LobbyService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Composable
fun CreateGameComp(
    gameName: String,
    gamePin: String,
    selectedNumberOfPlayers: Int,
    gameTime: Int,
    lobbyService: LobbyService,
    navController: NavController
) {
    Row(
        Modifier
            .padding(PaddingL)
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        val context = LocalContext.current
        Button(onClick = {
            createGameHandler(
                gameName,
                context,
                lobbyService,
                navController,
                gamePin,
                selectedNumberOfPlayers,
                gameTime
            )
        }) {
            Text(
                text = Translation.Button.CREATE
            )
        }
    }
}

private fun createGameHandler(
    gameName: String,
    androidContext: Context,
    provider: LobbyService,
    navController: NavController,
    gamePin: String,
    numOfPlayers: Int,
    gameTime: Int,
) {
    if (gameName == "") Toast.makeText(
        androidContext,
        Translation.CreateGame.CREATE_NO_GAME_NAME,
        Toast.LENGTH_SHORT,
    ).show()
    else {
        provider.createNewGame(gameName, gamePin, numOfPlayers, gameTime)
            .thenAccept { creationResult ->
                val toastMessage = when (creationResult) {
                    is CreateNewGameResult.Success ->
                        Translation.CreateGame.CREATE_SUCCESS

                    CreateNewGameResult.NameAlreadyExists ->
                        Translation.CreateGame.CREATE_NAME_ALREADY_EXISTS

                    CreateNewGameResult.OtherError ->
                        Translation.CreateGame.CREATE_OTHER_ERROR
                }

                CoroutineScope(Dispatchers.Main).launch {
                    Toast.makeText(
                        androidContext,
                        toastMessage,
                        Toast.LENGTH_SHORT,
                    ).show()

                    if (creationResult is CreateNewGameResult.Success) {
                        println(creationResult.lobbyId)
                        navController.navigate(
                            MenuRoute.Lobby.routeWithArgument(lobbyId = creationResult.lobbyId),
                        )
                    }
                }
            }
    }
}