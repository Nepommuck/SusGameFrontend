package edu.agh.susgame.front.gui.components.menu.components.createlobby

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import edu.agh.susgame.front.gui.components.common.theme.Header
import edu.agh.susgame.front.gui.components.common.theme.MenuBackground
import edu.agh.susgame.front.gui.components.common.theme.PaddingL
import edu.agh.susgame.front.gui.components.common.theme.TextStyler
import edu.agh.susgame.front.gui.components.common.util.Translation
import edu.agh.susgame.front.gui.components.menu.components.createlobby.elements.CreateGameComp
import edu.agh.susgame.front.gui.components.menu.components.createlobby.elements.GameNameComp
import edu.agh.susgame.front.gui.components.menu.components.createlobby.elements.PlayersNumberComp
import edu.agh.susgame.front.gui.components.menu.components.searchlobby.elements.ReturnButton
import edu.agh.susgame.front.service.interfaces.LobbyService


private const val DEFAULT_PLAYERS_AMOUNT = 4
private const val DEFAULT_GAME_TIME = 10

@Composable
fun CreateLobbyView(
    lobbyService: LobbyService,
    navController: NavController,
) {
    var gameName by remember { mutableStateOf(Translation.CreateGame.DEFAULT_GAME_NAME) }
    var gamePin by remember { mutableStateOf("") }
    var showPassword by remember { mutableStateOf(value = false) }
    var selectedNumberOfPlayers by remember { mutableIntStateOf(DEFAULT_PLAYERS_AMOUNT) }
    var gameTime by remember { mutableIntStateOf(DEFAULT_GAME_TIME) }
    var isGamePinEnabled by remember { mutableStateOf(false) }

    MenuBackground()

    Column(
        Modifier
            .fillMaxSize()
            .padding(PaddingL),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.weight(0.1f))
        Header(
            title = Translation.Menu.CREATE_GAME, style = TextStyler.TerminalXL
        )
        Spacer(modifier = Modifier.weight(0.1f))
        Box(
            modifier = Modifier
                .fillMaxSize()
                .weight(3.5f)
        ) {

            Column(
                Modifier
                    .padding(PaddingL)
                    .fillMaxWidth(0.5f)
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                GameNameComp(
                    gameName = gameName,
                    onGameNameChange = { gameName = it }
                )

//                GamePinComp(
//                    gamePin = gamePin,
//                    showPassword = showPassword,
//                    isGamePinEnabled = isGamePinEnabled,
//                    onGamePinChange = { gamePin = it },
//                    onShowPasswordChange = { showPassword = it },
//                    onGamePinEnabledChange = { isGamePinEnabled = it }
//                )


//                NumberOfPlayersComp(
//                    numberOfPlayers = selectedNumberOfPlayers,
//                    onNumberOfPlayersChange = { selectedNumberOfPlayers = it }
//                )

                PlayersNumberComp(
                    numOfPlayers = selectedNumberOfPlayers,
                    onGameTimeChange = { selectedNumberOfPlayers = it }
                )

                CreateGameComp(
                    gameName = gameName,
                    gamePin = gamePin,
                    selectedNumberOfPlayers = selectedNumberOfPlayers,
                    gameTime = gameTime,
                    lobbyService = lobbyService,
                    navController = navController
                )
            }
        }
        Box(
            modifier = Modifier
                .fillMaxSize()
                .weight(1f)
        ) {
            ReturnButton(navController = navController)
        }
    }
//    ReturnButton(navController = navController)
}