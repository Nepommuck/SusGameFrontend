package edu.agh.susgame.front.managers.state

import androidx.compose.runtime.mutableStateOf


class LobbyStateManager {
    val isGameMapReady = mutableStateOf(false)

    val isColorBeingChanged = mutableStateOf(false)

    val hasPlayerJoined = mutableStateOf(false)

    val areAllPlayersReady = mutableStateOf(false)
}
