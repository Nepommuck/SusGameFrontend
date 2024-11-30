package edu.agh.susgame.front.managers

import androidx.compose.runtime.mutableStateOf
import edu.agh.susgame.front.gui.components.common.graph.node.Node
import edu.agh.susgame.front.gui.components.game.components.computer.ComputerState


class GameStateManager {
    val currentlyInspectedNode = mutableStateOf<Node?>(null)

    val computerState = mutableStateOf<ComputerState>(ComputerState.NothingOpened)

    val isPathBeingChanged = mutableStateOf(false)

    val isComputerViewVisible = mutableStateOf(false)
}
