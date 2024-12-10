package edu.agh.susgame.front.managers.state

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import edu.agh.susgame.dto.socket.common.GameStatus
import edu.agh.susgame.front.gui.components.common.graph.node.Node
import edu.agh.susgame.front.managers.state.util.ComputerState


class GameStateManager {
    val gameStatus: MutableState<GameStatus> = mutableStateOf(GameStatus.WAITING)
    val computerState: MutableState<ComputerState> = mutableStateOf(ComputerState.NothingOpened)
    val currentlyInspectedNode: MutableState<Node?> = mutableStateOf(null)

    val isPathBeingChanged: MutableState<Boolean> = mutableStateOf(false)
    val isComputerViewVisible: MutableState<Boolean> = mutableStateOf(false)
    val isMenuOpened: MutableState<Boolean> = mutableStateOf(false)
}
