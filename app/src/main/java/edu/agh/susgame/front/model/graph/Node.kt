package edu.agh.susgame.front.model.graph

import edu.agh.susgame.front.util.Coordinates

open class Node(
    val id: Int,
    val name: String,
    val position: Coordinates,
)

class Router(
    id: Int,
    name: String,
    position: Coordinates,
    var bufferSize: Int,
    var bufferCurrentPackets: Int = 0,
) : Node(id, name, position)

class Host(
    id: Int,
    name: String,
    position: Coordinates,
    val hostId: Int,
    var packetsToSend: Int = 0,
) : Node(id, name, position)

class Server(
    id: Int,
    name: String,
    position: Coordinates,
    val packetsToWin: Int,
    var packetsReceived: Int = 0,
) : Node(id, name, position)
