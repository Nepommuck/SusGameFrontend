package edu.agh.susgame.front.gui.components.common.util

import androidx.activity.compose.BackHandler
import androidx.compose.runtime.Composable

@Composable
fun PreventNavigationBack(navigationActive: Boolean = false) {
    BackHandler(enabled = !navigationActive) {}
}
