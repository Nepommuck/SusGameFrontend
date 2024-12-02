package edu.agh.susgame.front.gui.components.common.graph.node

import androidx.compose.runtime.MutableIntState
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import edu.agh.susgame.front.gui.components.common.util.Coordinates
import edu.agh.susgame.front.gui.components.common.util.Translation

class Router(
    id: NodeId,
    name: String,
    position: Coordinates,
    val bufferSize: MutableState<Int>,
) : Node(id, name, position) {
    val bufferCurrentPackets: MutableState<Int> = mutableIntStateOf(0)
    val upgradeCost: MutableState<Int> = mutableIntStateOf(0)
    val isWorking: MutableState<Boolean> = mutableStateOf(true)
    val overheatLevel: MutableState<Int> = mutableIntStateOf(0)
    override fun getInfo(): String {
        return """
            ${Translation.Game.ROUTER}
            ${Translation.Game.STATE}: ${if (isWorking.value) Translation.Game.RUNNING else Translation.Game.SHUTDOWN}
            ${Translation.Game.BUFFER_STATE}: ${bufferCurrentPackets.value}/${bufferSize.value}
            ${Translation.Game.UPGRADE_COST}: ${upgradeCost.value}
        """.trimIndent()
    }
}