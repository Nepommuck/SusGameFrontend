package edu.agh.susgame.front.ui.component.menu.search

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.rememberScrollState
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
import edu.agh.susgame.dto.rest.model.Lobby
import edu.agh.susgame.dto.rest.model.LobbyId
import edu.agh.susgame.front.navigation.MenuRoute
import edu.agh.susgame.front.service.interfaces.LobbyService
import edu.agh.susgame.front.ui.Translation
import edu.agh.susgame.front.ui.component.common.Header


@Composable
fun SearchLobbiesView(
    lobbyService: LobbyService,
    navController: NavController
) {
    var awaitingGames by remember { mutableStateOf<Map<LobbyId, Lobby>?>(null) }
    var isLoading by remember { mutableStateOf(true) }


    LaunchedEffect(Unit) {
        lobbyService.getAll()
            .thenAccept {
                awaitingGames = it
                isLoading = false
            }
    }

    Column {
        Header(title = Translation.Lobby.FIND_GAME)
        if (isLoading) {
            Text(text = "${Translation.Button.LOADING}...")
        }
        Column(
            modifier = Modifier
                .verticalScroll(rememberScrollState())
                .weight(1f)
                .fillMaxHeight()
        ) {
            awaitingGames?.forEach {
                LobbyRowComponent(it.value, navController)
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
