package edu.agh.susgame.front.gui.components.common.graph.node

import edu.agh.susgame.dto.rest.model.PlayerId
import edu.agh.susgame.front.gui.components.common.util.Coordinates
import edu.agh.susgame.front.gui.components.common.util.Translation

class Host(
    id: NodeId,
    name: String,
    position: Coordinates,
    val playerId: PlayerId,
    private var packetsToSend: Int = 0,
) : Node(id, name, position) {
    override fun getInfo(): String {
        return """
            ${Translation.Game.HOST}
            ${Translation.Game.PACKETS_TO_SEND}: $packetsToSend
        """.trimIndent()
    }
}