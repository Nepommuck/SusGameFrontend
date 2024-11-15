package edu.agh.susgame.front.ui.components.common.graph.node

import edu.agh.susgame.dto.rest.model.PlayerId
import edu.agh.susgame.front.ui.components.common.util.Translation
import edu.agh.susgame.front.ui.components.common.util.Coordinates

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