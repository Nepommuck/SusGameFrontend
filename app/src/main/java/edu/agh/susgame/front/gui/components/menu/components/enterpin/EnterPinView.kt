package edu.agh.susgame.front.gui.components.menu.components.enterpin

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import edu.agh.susgame.dto.rest.model.LobbyId
import edu.agh.susgame.front.gui.components.common.theme.MenuBackground
import edu.agh.susgame.front.gui.components.menu.components.enterpin.elements.PinInput
import edu.agh.susgame.front.service.interfaces.LobbyService

@Composable
fun EnterPinView(
    lobbyId: LobbyId,
    lobbyService: LobbyService,
    navController: NavController
) {
    MenuBackground()

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center,
    ) {
        PinInput(lobbyId, lobbyService, navController)
    }
}
