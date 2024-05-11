package edu.agh.susgame.front.ui.component.menu.search

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import edu.agh.susgame.front.providers.interfaces.AwaitingGamesProvider
import edu.agh.susgame.front.ui.Translation
import edu.agh.susgame.front.ui.component.common.Header
import edu.agh.susgame.front.ui.component.menu.navigation.MenuRoute
import edu.agh.susgame.front.ui.theme.PaddingL


@Composable
fun SearchGamesView(
    awaitingGamesProvider: AwaitingGamesProvider,
    navController: NavController
) {
    val awaitingGames = awaitingGamesProvider.getAll()

    Column(Modifier.padding(PaddingL)) {
        Header(title = Translation.Menu.SearchGame.FIND_GAME)
        Column(
            modifier = Modifier
                .verticalScroll(ScrollState(0))
                .weight(1f)
                .fillMaxHeight()
        ) {
            awaitingGames.forEach {
                AwaitingGameRowComponent(it, navController)
            }
        }
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth(),
        ) {
            Button(onClick = {
                navController.navigate(MenuRoute.MainMenu.route)
            }) {
                Text(text = Translation.Button.GO_BACK)
            }
        }
    }
}
