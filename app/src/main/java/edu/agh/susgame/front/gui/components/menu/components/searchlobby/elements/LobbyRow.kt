package edu.agh.susgame.front.gui.components.menu.components.searchlobby.elements

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import edu.agh.susgame.R
import edu.agh.susgame.dto.rest.model.LobbyRow
import edu.agh.susgame.front.gui.components.common.theme.TextStyler
import edu.agh.susgame.front.gui.components.common.theme.Transparent
import edu.agh.susgame.front.gui.components.menu.navigation.MenuRoute


@Composable
internal fun LobbyRow(
    lobby: LobbyRow,
    navController: NavController
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(50.dp)
            .clickable {
                if (lobby.isPinSetUp) {
                    navController.navigate(
                        MenuRoute.EnterPin.routeWithArgument(lobbyId = lobby.id)
                    )
                } else {
                    navController.navigate(
                        MenuRoute.Lobby.routeWithArgument(lobbyId = lobby.id)
                    )
                }
            },
        contentAlignment = Alignment.Center
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            Image(
                painter = painterResource(R.drawable.frame_long),
                contentDescription = null,
                modifier = Modifier
                    .fillMaxSize(),
                contentScale = ContentScale.FillBounds
            )
        }
        Row(
            modifier = Modifier
                .fillMaxWidth(0.9f),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .fillMaxHeight(0.5f)
                    .weight(1f),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(R.drawable.padlock),
                    contentDescription = null,
                    contentScale = ContentScale.Fit,
                    colorFilter = ColorFilter.tint(
                        color = if (lobby.isPinSetUp) Color.White.copy(alpha = 0.6f)
                        else Transparent
                    )
                )
            }
            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .weight(10f),
                contentAlignment = Alignment.CenterStart
            ) {
                Text(
                    text = lobby.name,
                    style = TextStyler.TerminalL,
                )
            }
            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .weight(1f),
                contentAlignment = Alignment.Center
            )
            {
                Text(
                    text = "${lobby.playersWaitingCount}/${lobby.maxNumOfPlayers}",
                    style = TextStyler.TerminalL,
                )
            }
        }
    }
}
