package edu.agh.susgame.front.gui.components.menu.components.createlobby.elements

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import edu.agh.susgame.front.gui.components.common.theme.TextStyler
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
    lobbyService: LobbyService,
    navController: NavController
) {
    val context = LocalContext.current
    Button(
        onClick = {
            createGameHandler(
                gameName,
                context,
                lobbyService,
                navController,
                gamePin,
                selectedNumberOfPlayers,
            )
        },
        modifier = Modifier.wrapContentSize(),
        colors = ButtonDefaults.buttonColors(
            containerColor = Color.Green.copy(alpha = 0.5f),
        ),
        shape = RoundedCornerShape(16.dp)
    ) {
        Text(text = Translation.Button.CREATE, style = TextStyler.TerminalM, color = Color.White)
    }
}


private fun createGameHandler(
    gameName: String,
    androidContext: Context,
    provider: LobbyService,
    navController: NavController,
    gamePin: String,
    numOfPlayers: Int,
) {
    if (gameName == "") Toast.makeText(
        androidContext,
        Translation.CreateGame.CREATE_NO_GAME_NAME,
        Toast.LENGTH_SHORT,
    ).show()
    else {
        provider.createNewGame(gameName, gamePin, numOfPlayers)
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
                        navController.navigate(
                            MenuRoute.Lobby.routeWithArgument(lobbyId = creationResult.lobbyId),
                        )
                    }
                }
            }
    }
}