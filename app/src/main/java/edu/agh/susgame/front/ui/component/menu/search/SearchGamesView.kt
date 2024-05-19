package edu.agh.susgame.front.ui.component.menu.search

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import edu.agh.susgame.front.model.game.AwaitingGame
import edu.agh.susgame.front.navigation.MenuRoute
import edu.agh.susgame.front.providers.interfaces.AwaitingGamesProvider
import edu.agh.susgame.front.ui.Translation
import edu.agh.susgame.front.ui.component.common.Header


@Composable
fun SearchGamesView(
    awaitingGamesProvider: AwaitingGamesProvider,
    navController: NavController
) {
    var awaitingGames by remember { mutableStateOf<List<AwaitingGame>?>(null) }
    var isLoading by remember { mutableStateOf(true) }

    LaunchedEffect(Unit) {
        awaitingGamesProvider.getAll()
            .thenAccept {
                awaitingGames = it
                isLoading = false
            }
    }

    Column {
        Header(title = Translation.Menu.SearchGame.FIND_GAME)
        if (isLoading) {
            Text(text = "${Translation.Button.LOADING}...")
        }
        Column(
            modifier = Modifier
                .verticalScroll(ScrollState(0))
                .weight(1f)
                .fillMaxHeight()
        ) {
            awaitingGames?.forEach {
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
