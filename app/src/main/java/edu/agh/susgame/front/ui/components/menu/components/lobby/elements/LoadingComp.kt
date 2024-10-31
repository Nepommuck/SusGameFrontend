package edu.agh.susgame.front.ui.components.menu.components.lobby.elements

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import edu.agh.susgame.front.Translation

@Composable
fun LoadingComp() {
    Text(text = "${Translation.Button.LOADING}...")
}