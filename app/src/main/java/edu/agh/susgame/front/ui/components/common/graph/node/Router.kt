package edu.agh.susgame.front.ui.components.common.graph.node

import edu.agh.susgame.front.ui.components.common.util.Translation
import edu.agh.susgame.front.ui.components.common.util.Coordinates

class Router(
    id: NodeId,
    name: String,
    position: Coordinates,
    private var bufferSize: Int,
    private var bufferCurrentPackets: Int = 0,
) : Node(id, name, position) {
    override fun getInfo(): String {
        return """
            ${Translation.Game.ROUTER}
            ${Translation.Game.BUFFER_SIZE}: $bufferSize
            ${Translation.Game.BUFFER_CURRENT_PACKETS}: $bufferCurrentPackets
        """.trimIndent()
    }
}