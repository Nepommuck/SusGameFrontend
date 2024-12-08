package edu.agh.susgame.front.gui.components.menu.components.searchlobby.elements

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import edu.agh.susgame.dto.rest.model.Lobby
import edu.agh.susgame.front.gui.components.common.theme.PaddingM
import edu.agh.susgame.front.gui.components.common.theme.TextStyler
import edu.agh.susgame.front.gui.components.menu.navigation.MenuRoute

@Composable
internal fun LobbyRow(
    lobby: Lobby,
    navController: NavController
) {
    Box(
        modifier = Modifier
            .padding(bottom = PaddingM)
            .clickable {
                navController.navigate(
                    MenuRoute.Lobby.routeWithArgument(lobbyId = lobby.id)
                )
            },
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Text(text = lobby.name, style = TextStyler.TerminalL)
            Text(
                text = "${lobby.playersWaiting.size}/${lobby.maxNumOfPlayers}",
                style = TextStyler.TerminalL
            )
        }
//        }
    }
}