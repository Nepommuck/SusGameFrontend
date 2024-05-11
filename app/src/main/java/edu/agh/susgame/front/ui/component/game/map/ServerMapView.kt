package edu.agh.susgame.front.ui.component.game.map

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import edu.agh.susgame.front.model.game.GameId
import edu.agh.susgame.front.providers.interfaces.ServerMapProvider
import edu.agh.susgame.front.ui.theme.PaddingL

@Composable
fun ServerMapView(gameId: GameId?, serverMapProvider: ServerMapProvider) {
    Column(
        modifier = Modifier
            .padding(top = PaddingL, start = PaddingL, end = PaddingL)
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.fillMaxHeight()
        ) {
            ServerMapComponent(serverMapProvider)
        }
    }
}
