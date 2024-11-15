package edu.agh.susgame.front.ui.components.menu.components.searchlobby.elements

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import edu.agh.susgame.dto.rest.model.Lobby
import edu.agh.susgame.front.ui.components.common.navigation.MenuRoute
import edu.agh.susgame.front.ui.components.common.theme.PaddingM

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
        Column(
            modifier = Modifier
                .border(
                    color = Color.Black,
                    width = 1.dp,
                    shape = RoundedCornerShape(10.dp),
                )
                .padding(PaddingM)
                .fillMaxWidth()
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                Text(text = lobby.name, fontWeight = FontWeight.Bold)
                Text(text = "${lobby.playersWaiting.size}/${lobby.maxNumOfPlayers}")
            }
        }
    }
}
