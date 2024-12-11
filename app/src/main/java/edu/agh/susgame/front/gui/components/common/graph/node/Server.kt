package edu.agh.susgame.front.gui.components.common.graph.node

import androidx.compose.runtime.MutableIntState
import androidx.compose.runtime.mutableIntStateOf
import edu.agh.susgame.front.gui.components.common.util.Coordinates
import edu.agh.susgame.front.gui.components.common.util.Translation

class Server(
    id: NodeId,
    name: String,
    position: Coordinates,
    val packetsToWin: Int,
    val packetsReceived: MutableIntState = mutableIntStateOf(0)
) : Node(id, name, position) {

    override fun getNodeName(): String = Translation.Game.SERVER

    fun getName1(): String {
        return """
            ${Translation.Game.SERVER}
            ${Translation.Game.RECEIVED_DATA}: ${packetsReceived.intValue}/$packetsToWin
        """.trimIndent()
    }
}