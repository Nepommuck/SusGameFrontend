package edu.agh.susgame.front.gui.components.menu.components.lobby.elements.components

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import edu.agh.susgame.dto.rest.model.LobbyId
import edu.agh.susgame.front.gui.components.common.util.Translation
import edu.agh.susgame.front.gui.components.menu.navigation.MenuRoute

@Composable
fun FailedToLoadComp(lobbyId: LobbyId, navController: NavController) {
    Column {
        Text(text = Translation.Error.failedToLoadGame(lobbyId))
        Button(onClick = {
            navController.navigate(MenuRoute.FindGame.route)
        }) {
            Text(text = Translation.Button.GO_BACK)
        }
    }
}