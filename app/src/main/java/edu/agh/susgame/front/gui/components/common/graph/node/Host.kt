package edu.agh.susgame.front.gui.components.common.graph.node

import androidx.compose.runtime.MutableState
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

    val packetsToSend: MutableState<Int> = mutableIntStateOf(0)
    val maxPacketsToSend: MutableState<Int> = mutableIntStateOf(5)
    override fun getInfo(): String {
        return """
            ${Translation.Game.HOST}: $name
            ${Translation.Game.PACKETS_TO_SEND}: ${packetsToSend.value}/${maxPacketsToSend.value}
        """.trimIndent()
    }
}