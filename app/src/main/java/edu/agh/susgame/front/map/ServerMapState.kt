package edu.agh.susgame.front.map

import edu.agh.susgame.front.util.Coordinates

class ServerInfo(
    val name: String,
    val position: Coordinates,
)

class Connection(
    val serverA: ServerInfo,
    val serverB: ServerInfo,
)

class ServerMapState(
    val mapSize: Coordinates,
    val serves: List<ServerInfo>,
    val connections: List<Connection>,
)
