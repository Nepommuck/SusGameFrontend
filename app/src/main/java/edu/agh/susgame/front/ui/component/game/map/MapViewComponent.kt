package edu.agh.susgame.front.ui.component.game.map

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import edu.agh.susgame.front.providers.interfaces.ServerMapProvider
import edu.agh.susgame.front.ui.theme.PaddingL

@Composable
fun MapViewComponent(serverMapProvider: ServerMapProvider) {
    Column(
        modifier = Modifier
            .padding(PaddingL)
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.fillMaxHeight()
        ) {
            ServerMapComponent(serverMapProvider)
        }
        Button(onClick = {
        }) {
            Text("Add another one")
        }
    }
}
