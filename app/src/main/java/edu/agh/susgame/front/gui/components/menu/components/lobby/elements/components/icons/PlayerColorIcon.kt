package edu.agh.susgame.front.gui.components.menu.components.lobby.elements.components.icons

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color

@Composable
fun PlayerColorIcon(
    playerColor: MutableState<Color>,
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(playerColor.value)
    )
}