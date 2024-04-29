package edu.agh.susgame.front.providers.interfaces

import androidx.compose.runtime.MutableState
import edu.agh.susgame.front.model.ServerMapState

interface ServerMapProvider {
    fun getServerMapState(): MutableState<ServerMapState>
}

