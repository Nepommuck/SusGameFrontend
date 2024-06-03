package edu.agh.susgame.front.model.graph

import edu.agh.susgame.front.util.Coordinates

abstract class Node(
    val id: Int,
    val name: String,
    val position: Coordinates,
) {
    abstract fun getInfo(): String
}

class Router(
    id: Int,
    name: String,
    position: Coordinates,
    private var bufferSize: Int,
    private var bufferCurrentPackets: Int = 0,
) : Node(id, name, position) {
    override fun getInfo(): String { // fow now it's in string, later on this should be improved to something more accurate
        return "Id: $id\nName: $name\nBufferSize: $bufferSize\nBufferCurrentPackets: $bufferCurrentPackets"
    }
}

class Host(
    id: Int,
    name: String,
    position: Coordinates,
    private val hostId: Int,
    private var packetsToSend: Int = 0,
) : Node(id, name, position) {
    override fun getInfo(): String {
        return "Id: $id\nName: $name\nHostId: $hostId\nPacketsToSend: $packetsToSend"
    }
}

class Server(
    id: Int,
    name: String,
    position: Coordinates,
    private val packetsToWin: Int,
    private var packetsReceived: Int = 0,
) : Node(id, name, position) {
    override fun getInfo(): String {
        return "Id: $id\nName: $name\nPacketsToWin: $packetsToWin\nPacketsReceived: $packetsReceived"
    }
}







