package edu.agh.susgame.front.ui.component.menu.search

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
import edu.agh.susgame.front.model.game.AwaitingGame
import edu.agh.susgame.front.model.game.MaxPlayersPerGame
import edu.agh.susgame.front.ui.component.menu.navigation.MenuRoute
import edu.agh.susgame.front.ui.theme.PaddingM

@Composable
internal fun AwaitingGameRowComponent(awaitingGame: AwaitingGame, navController: NavController) {
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
                Text(text = "${awaitingGame.playersWaiting.size}/$MaxPlayersPerGame")
            }
        }
    }
}
