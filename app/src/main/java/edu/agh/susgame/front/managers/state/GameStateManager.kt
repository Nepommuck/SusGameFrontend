package edu.agh.susgame.front.managers.state

import androidx.compose.runtime.mutableStateOf
import edu.agh.susgame.dto.socket.common.GameStatus
import edu.agh.susgame.front.gui.components.common.graph.node.Node
import edu.agh.susgame.front.managers.state.util.ComputerState


class GameStateManager {
    val gameStatus = mutableStateOf(GameStatus.WAITING)

    val computerState = mutableStateOf<ComputerState>(ComputerState.NothingOpened)

    val currentlyInspectedNode = mutableStateOf<Node?>(null)

    val isPathBeingChanged = mutableStateOf(false)
    val isComputerViewVisible = mutableStateOf(false)
}
