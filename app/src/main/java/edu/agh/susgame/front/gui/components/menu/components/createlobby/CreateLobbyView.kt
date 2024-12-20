package edu.agh.susgame.front.gui.components.menu.components.createlobby

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import edu.agh.susgame.dto.rest.model.LobbyPin
import edu.agh.susgame.front.gui.components.common.theme.Header
import edu.agh.susgame.front.gui.components.common.theme.MenuBackground
import edu.agh.susgame.front.gui.components.common.theme.MenuButton
import edu.agh.susgame.front.gui.components.common.theme.PaddingM
import edu.agh.susgame.front.gui.components.common.theme.PaddingXL
import edu.agh.susgame.front.gui.components.common.theme.TextStyler
import edu.agh.susgame.front.gui.components.common.util.Translation
import edu.agh.susgame.front.gui.components.menu.components.createlobby.elements.CreateGameComp
import edu.agh.susgame.front.gui.components.menu.components.createlobby.elements.GameNameComp
import edu.agh.susgame.front.gui.components.menu.components.createlobby.elements.GamePinComp
import edu.agh.susgame.front.gui.components.menu.components.createlobby.elements.PlayersNumberComp
import edu.agh.susgame.front.gui.components.menu.navigation.MenuRoute
import edu.agh.susgame.front.service.interfaces.LobbyService


private const val DEFAULT_PLAYERS_AMOUNT = 4

@Composable
fun CreateLobbyView(
    lobbyService: LobbyService,
    navController: NavController,
) {
    var gameName by remember { mutableStateOf(Translation.CreateGame.DEFAULT_GAME_NAME) }
    var showPassword by remember { mutableStateOf(value = false) }
    var selectedNumberOfPlayers by remember { mutableIntStateOf(DEFAULT_PLAYERS_AMOUNT) }
    var isGamePinEnabled by remember { mutableStateOf(false) }
    var gamePinInputValue by remember { mutableStateOf("") }

    MenuBackground()

    Column(
        Modifier
            .fillMaxSize()
            .padding(PaddingM),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Header(
            title = Translation.Menu.CREATE_GAME, style = TextStyler.TerminalXL
        )
        Box(
            modifier = Modifier
                .fillMaxSize()
        ) {
            Row(
                modifier = Modifier.fillMaxSize(),
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxHeight(0.9f)
                        .padding(PaddingXL)
                        .weight(3f),
                    verticalArrangement = Arrangement.SpaceEvenly,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    GameNameComp(
                        gameName = gameName,
                        onGameNameChange = { gameName = it }
                    )
                    GamePinComp(
                        gamePin = gamePinInputValue,
                        showPassword = showPassword,
                        isGamePinEnabled = isGamePinEnabled,
                        onGamePinChange = { gamePinInputValue = it },
                        onShowPasswordChange = { showPassword = it },
                        onGamePinEnabledChange = { isGamePinEnabled = it }
                    )
                    PlayersNumberComp(
                        numOfPlayers = selectedNumberOfPlayers,
                        onGameTimeChange = { selectedNumberOfPlayers = it }
                    )
                    CreateGameComp(
                        gameName = gameName,
                        gamePin = if (isGamePinEnabled) LobbyPin(gamePinInputValue) else null,
                        selectedNumberOfPlayers = selectedNumberOfPlayers,
                        lobbyService = lobbyService,
                        navController = navController
                    )
                }
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .weight(2f),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // TODO Add some content here
                }
            }
        }
    }
    MenuButton(
        text = Translation.Button.GO_BACK,
        onClick = { navController.navigate(MenuRoute.MainMenu.route) },
        alignment = Alignment.BottomStart
    )
}