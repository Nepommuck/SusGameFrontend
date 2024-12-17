package edu.agh.susgame.front.gui.components.common.graph.node

import androidx.compose.runtime.MutableIntState
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import edu.agh.susgame.dto.rest.model.PlayerId
import edu.agh.susgame.front.gui.components.common.util.Coordinates
import edu.agh.susgame.front.gui.components.common.util.Translation

class Router(
    id: NodeId,
    position: Coordinates,
    val bufferSize: MutableIntState,
) : Node(id, position) {
    override val name: String = Translation.Game.ROUTER

    val bufferCurrentPackets: MutableIntState = mutableIntStateOf(0)
    val upgradeCost: MutableIntState = mutableIntStateOf(0)
    val isWorking: MutableState<Boolean> = mutableStateOf(true)
    val overheat: MutableIntState = mutableIntStateOf(0)
    val playersSet: MutableSet<PlayerId> = mutableSetOf()

    fun getState(): String =
        if (isWorking.value) Translation.Game.RUNNING else Translation.Game.SHUTDOWN

    fun getBuffer(): String = "${bufferCurrentPackets.intValue}/${bufferSize.intValue}"

    override fun getInfo(): String {
        return """
            ${Translation.Game.STATE}: ${if (isWorking.value) Translation.Game.RUNNING else Translation.Game.SHUTDOWN}
            ${Translation.Game.BUFFER_STATE}: ${bufferCurrentPackets.intValue}/${bufferSize.intValue}
            ${Translation.Game.UPGRADE_COST}: ${upgradeCost.intValue}
        """.trimIndent()
    }
}
