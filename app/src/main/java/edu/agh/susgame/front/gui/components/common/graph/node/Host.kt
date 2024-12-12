package edu.agh.susgame.front.gui.components.common.graph.node

import androidx.compose.runtime.MutableIntState
import androidx.compose.runtime.mutableIntStateOf
import edu.agh.susgame.dto.rest.model.PlayerId
import edu.agh.susgame.front.gui.components.common.util.Coordinates
import edu.agh.susgame.front.gui.components.common.util.Translation

class Host(
    id: NodeId,
    name: String,
    position: Coordinates,
    val playerId: PlayerId,
) : Node(id, name, position) {

    val packetsToSend: MutableIntState = mutableIntStateOf(0)
    val maxPacketsToSend: MutableIntState = mutableIntStateOf(5)
    override fun getNodeName(): String = "${Translation.Game.HOST}: $name"
    fun getFlow():String = "${packetsToSend.intValue}/${maxPacketsToSend.intValue}"
    override fun getInfo(): String {
        return """
            ${Translation.Game.PACKETS_TO_SEND}: ${packetsToSend.intValue}/${maxPacketsToSend.intValue}
        """.trimIndent()
    }

}