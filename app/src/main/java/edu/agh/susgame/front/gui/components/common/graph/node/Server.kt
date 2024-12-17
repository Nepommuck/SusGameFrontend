package edu.agh.susgame.front.gui.components.common.graph.node

import androidx.compose.runtime.MutableIntState
import androidx.compose.runtime.mutableIntStateOf
import edu.agh.susgame.front.gui.components.common.util.Coordinates
import edu.agh.susgame.front.gui.components.common.util.Translation

class Server(
    id: NodeId,
    position: Coordinates,
    val packetsToWin: Int,
    val packetsReceived: MutableIntState = mutableIntStateOf(0)
) : Node(id, position) {

    override val name: String = Translation.Game.SERVER

    override fun getInfo(): String {
        return """
            ${Translation.Game.RECEIVED_DATA}: ${packetsReceived.intValue}/$packetsToWin
        """.trimIndent()
    }
}
