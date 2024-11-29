package edu.agh.susgame.front.gui.components.game.components.computer.desktop

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import edu.agh.susgame.R
import edu.agh.susgame.front.gui.components.game.components.computer.desktop.components.DesktopIconButton
import edu.agh.susgame.front.gui.components.game.components.computer.desktop.components.DesktopIconMock


@Composable
fun DesktopComponent() {
    var msg by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color.Blue),
        verticalArrangement = Arrangement.SpaceEvenly
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly,
        ) {
            DesktopIconButton(
                painter = painterResource(id = R.drawable.computer_icon_tree),
                imageDescription = "icon-tree",
                onClick = { msg = "$msg#" },
            )

            DesktopIconButton(
                painter = painterResource(id = R.drawable.computer_icon_gear),
                imageDescription = "icon-gear",
                onClick = { msg = "$msg#" },
            )
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly,
        ) {
            DesktopIconButton(
                painter = painterResource(id = R.drawable.computer_icon_themes),
                imageDescription = "icon-themes",
                onClick = { msg = "$msg#" },
            )

            DesktopIconMock()
        }
    }
}
