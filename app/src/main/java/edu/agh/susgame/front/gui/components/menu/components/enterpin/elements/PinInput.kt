package edu.agh.susgame.front.gui.components.menu.components.enterpin.elements

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.navigation.NavController
import edu.agh.susgame.dto.rest.model.LobbyId
import edu.agh.susgame.dto.rest.model.LobbyPin
import edu.agh.susgame.front.gui.components.common.theme.MenuButton
import edu.agh.susgame.front.gui.components.common.theme.TextStyler
import edu.agh.susgame.front.gui.components.common.util.Translation
import edu.agh.susgame.front.gui.components.menu.navigation.MenuRoute
import edu.agh.susgame.front.service.interfaces.GetGameDetailsResult.InvalidPin
import edu.agh.susgame.front.service.interfaces.GetGameDetailsResult.OtherError
import edu.agh.susgame.front.service.interfaces.GetGameDetailsResult.Success
import edu.agh.susgame.front.service.interfaces.LobbyService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


@Composable
fun PinInput(
    lobbyId: LobbyId,
    lobbyService: LobbyService,
    navController: NavController
) {
    Box(
        modifier = Modifier
            .fillMaxWidth(0.5f)
            .fillMaxHeight(0.4f),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize(),
            verticalArrangement = Arrangement.SpaceEvenly,
            horizontalAlignment = Alignment.CenterHorizontally

        ) {
            var currentPinInputValue by remember { mutableStateOf("") }
            var badPinProvided by remember { mutableStateOf(false) }
            Box(modifier = Modifier.weight(1f)) {
                OutlinedTextField(
                    label = {
                        Text(
                            text = Translation.Lobby.ENTER_GAME_PIN,
                            style = TextStyler.TerminalS,
                        )
                    },
                    value = currentPinInputValue,
                    textStyle = TextStyler.TerminalInput.copy(textAlign = TextAlign.Left),
                    onValueChange = { currentPinInputValue = it },
                    modifier = Modifier
                        .fillMaxWidth(0.8f)
                        .align(Alignment.Center),
                    singleLine = true
                )
            }
            Text(
                text = if (badPinProvided) Translation.Lobby.WRONG_PIN else "",
                color = Color.Red,
                style = TextStyler.TerminalS,
            )
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .weight(1f),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .weight(1f),
                    contentAlignment = Alignment.Center
                ) {
                    MenuButton(
                        text = Translation.Button.CANCEL,
                        onClick = {
                            navController.navigate(MenuRoute.FindGame.route)
                        })
                }

                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .weight(1f),
                    contentAlignment = Alignment.Center
                ) {
                    MenuButton(
                        text = Translation.Button.ACCEPT,
                        onClick = {
                            val lobbyPin = LobbyPin(currentPinInputValue)

                            lobbyService.getLobbyDetails(lobbyId, lobbyPin).thenApply { result ->
                                println(result)
                                when (result) {
                                    InvalidPin -> {
                                        badPinProvided = true
                                    }

                                    is Success -> {
                                        CoroutineScope(Dispatchers.Main).launch {
                                            navController.navigate(
                                                MenuRoute.Lobby.routeWithArgument(lobbyId, lobbyPin)
                                            )
                                        }
                                    }

                                    OtherError -> {
                                        navController.navigate(
                                            MenuRoute.FindGame.route
                                        )
                                    }
                                }
                            }
                        }
                    )
                }
            }
        }
    }
}
