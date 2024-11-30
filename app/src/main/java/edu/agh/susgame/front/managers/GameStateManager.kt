package edu.agh.susgame.front.managers

import androidx.compose.runtime.mutableStateOf
import edu.agh.susgame.front.gui.components.common.graph.node.Node


class GameStateManager {
    val isPathBeingChanged = mutableStateOf(false)

    val currentlyInspectedNode = mutableStateOf<Node?>(null)
}
