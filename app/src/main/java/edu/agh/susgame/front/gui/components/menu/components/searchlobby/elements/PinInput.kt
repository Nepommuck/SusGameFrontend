package edu.agh.susgame.front.gui.components.menu.components.searchlobby.elements

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
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.navigation.NavController
import edu.agh.susgame.dto.rest.model.LobbyId
import edu.agh.susgame.front.gui.components.common.theme.MenuButton
import edu.agh.susgame.front.gui.components.common.theme.TextStyler
import edu.agh.susgame.front.gui.components.common.util.Translation
import edu.agh.susgame.front.gui.components.menu.navigation.MenuRoute


@Composable
fun PinInput(
    currentLobbyIdWithPin: MutableState<LobbyId?>,
    navController: NavController
) {
    Box(
        modifier = Modifier
            .fillMaxWidth(0.5f)
            .fillMaxHeight(0.7f),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize(),
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            var pin by remember { mutableStateOf("") }

            OutlinedTextField(
                label = {
                    Text(
                        text = Translation.Lobby.ENTER_GAME_PIN,
                        style = TextStyler.TerminalS,
                    )
                },
                value = pin,
                textStyle = TextStyler.TerminalInput.copy(textAlign = TextAlign.Left),
                onValueChange = { pin = it },
                modifier = Modifier
                    .fillMaxWidth(0.8f),
                singleLine = true
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
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
                        onClick = { currentLobbyIdWithPin.value = null })
                }
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .weight(1f),
                    contentAlignment = Alignment.Center
                ) {
                    MenuButton(
                        text = Translation.Button.ACCEPT,
                        // TODO GAME-121 make this with PIN
                        onClick = {
                            currentLobbyIdWithPin.value?.let {
                                navController.navigate(
                                    MenuRoute.Lobby.routeWithArgument(
                                        lobbyId = it
                                    )
                                )
                            }
                        }
                    )
                }
            }
        }
    }
}
