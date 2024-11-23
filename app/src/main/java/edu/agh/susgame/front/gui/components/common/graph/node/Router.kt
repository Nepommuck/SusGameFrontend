package edu.agh.susgame.front.gui.components.common.graph.node

import edu.agh.susgame.front.gui.components.common.util.Coordinates
import edu.agh.susgame.front.gui.components.common.util.Translation

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