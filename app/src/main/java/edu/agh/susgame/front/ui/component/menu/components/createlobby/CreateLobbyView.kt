package edu.agh.susgame.front.ui.component.menu.components.createlobby

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import edu.agh.susgame.front.navigation.MenuRoute
import edu.agh.susgame.front.service.interfaces.LobbyService
import edu.agh.susgame.front.ui.Translation
import edu.agh.susgame.front.ui.component.common.Header
import edu.agh.susgame.front.ui.component.menu.components.createlobby.elements.CreateGameComp
import edu.agh.susgame.front.ui.component.menu.components.createlobby.elements.GameNameComp
import edu.agh.susgame.front.ui.component.menu.components.createlobby.elements.GamePinComp
import edu.agh.susgame.front.ui.component.menu.components.createlobby.elements.GameTimeComp
import edu.agh.susgame.front.ui.component.menu.components.createlobby.elements.NumberOfPlayersComp
import edu.agh.susgame.front.ui.theme.PaddingL


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

    Column(Modifier.padding(PaddingL)) {
        Column(
            Modifier
                .padding(PaddingL)
                .weight(1f)
                .verticalScroll(rememberScrollState())
        ) {

            Header(
                title = Translation.Menu.CREATE_GAME
            )

            GameNameComp(
                gameName = gameName,
                onGameNameChange = { gameName = it }
            )

            GamePinComp(
                gamePin = gamePin,
                onGamePinChange = { gamePin = it },
                showPassword = showPassword,
                onShowPasswordChange = { showPassword = it },
                isGamePinEnabled = isGamePinEnabled,
                onGamePinEnabledChange = { isGamePinEnabled = it }
            )


            NumberOfPlayersComp(
                numberOfPlayers = selectedNumberOfPlayers,
                onNumberOfPlayersChange = { selectedNumberOfPlayers = it }
            )

            GameTimeComp(
                gameTime = gameTime,
                onGameTimeChange = { gameTime = it }
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

        // go_back button
        Button(onClick = {
            navController.navigate(MenuRoute.MainMenu.route)
        }) {
            Text(text = Translation.Button.GO_BACK)
        }
    }
}


