package edu.agh.susgame.front.gui.components.game.components.computer.desktop

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import edu.agh.susgame.R
import edu.agh.susgame.front.gui.components.game.components.computer.desktop.components.DesktopIconButton
import edu.agh.susgame.front.gui.components.game.components.computer.desktop.components.DesktopIconPlaceholder
import edu.agh.susgame.front.managers.state.ComputerState
import edu.agh.susgame.front.managers.state.MiniGame


@Composable
fun DesktopComponent(computerState: MutableState<ComputerState>) {
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
                onClick = {
                    computerState.value = ComputerState.MiniGameOpened(MiniGame.MiniGame1)
                },
            )

            DesktopIconButton(
                painter = painterResource(id = R.drawable.computer_icon_gear),
                imageDescription = "icon-gear",
                onClick = {
                    computerState.value = ComputerState.MiniGameOpened(MiniGame.MiniGame2)
                },
            )
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly,
        ) {
            DesktopIconButton(
                painter = painterResource(id = R.drawable.computer_icon_envelope),
                imageDescription = "icon-envelope",
                onClick = { computerState.value = ComputerState.ChatOpened },
            )

            DesktopIconPlaceholder()
        }
    }
}
