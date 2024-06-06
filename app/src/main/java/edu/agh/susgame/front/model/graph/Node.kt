package edu.agh.susgame.front.model.graph

import edu.agh.susgame.front.ui.Translation
import edu.agh.susgame.front.util.Coordinates

data class NodeId(val value: Int)
abstract class Node(
    val id: NodeId,
    val name: String,
    val position: Coordinates,
) {
    abstract fun getInfo(): String // TODO fow now it's in string, later on this should be improved to something more accurate
}

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

class Host(
    id: NodeId,
    name: String,
    position: Coordinates,
    private val hostId: Int,
    private var packetsToSend: Int = 0,
) : Node(id, name, position) {
    override fun getInfo(): String {
        return """
            ${Translation.Game.HOST}
            ${Translation.Game.PACKETS_TO_SEND}: $packetsToSend
        """.trimIndent()
    }
}

class Server(
    id: NodeId,
    name: String,
    position: Coordinates,
    private val packetsToWin: Int,
    private var packetsReceived: Int = 0,
) : Node(id, name, position) {
    override fun getInfo(): String {
        return """
            ${Translation.Game.SERVER}
            ${Translation.Game.PACKETS_TO_WIN}: $packetsToWin
            ${Translation.Game.PACKETS_RECEIVED}: $packetsReceived
        """.trimIndent()
    }
}







