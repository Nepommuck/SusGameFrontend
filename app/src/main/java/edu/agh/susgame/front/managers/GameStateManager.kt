package edu.agh.susgame.front.managers

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf


class GameStateManager {
    val isPathBeingChanged: MutableState<Boolean> = mutableStateOf(false)
}
