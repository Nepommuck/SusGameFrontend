package edu.agh.susgame.front.ui.component.menu

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import edu.agh.susgame.front.model.AwaitingGame
import edu.agh.susgame.front.providers.interfaces.AwaitingGamesProvider
import edu.agh.susgame.front.ui.component.common.Header
import edu.agh.susgame.front.ui.component.menu.navigation.MenuRoute
import edu.agh.susgame.front.ui.theme.PaddingL
import edu.agh.susgame.front.ui.theme.PaddingM


@Composable
private fun AwaitingGameRowComponent(awaitingGame: AwaitingGame, navController: NavController) {
    Box(
        modifier = Modifier
            .padding(bottom = PaddingM)
            .clickable {
                navController.navigate("${MenuRoute.AwaitingGame.route}/${awaitingGame.id.value}")
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
                Text(text = awaitingGame.name, fontWeight = FontWeight.Bold)
                Text(text = "${awaitingGame.playersWaiting}/6")
            }
            Text(text = awaitingGame.description, maxLines = 1, overflow = TextOverflow.Ellipsis)
        }
    }
}


@Composable
fun SearchGamesComponent(
    awaitingGamesProvider: AwaitingGamesProvider,
    navController: NavController
) {
    val awaitingGames = awaitingGamesProvider.getAll()

    Column(Modifier.padding(PaddingL)) {
        Header(title = "Search for a game")
        Column(
            modifier = Modifier
                .verticalScroll(ScrollState(0)),
        ) {
            awaitingGames.forEach {
                AwaitingGameRowComponent(it, navController)
            }
        }
    }
}