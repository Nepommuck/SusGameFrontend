package edu.agh.susgame.front.model

import edu.agh.susgame.front.util.Coordinates

data class ServerInfo(
    val name: String,
    val position: Coordinates,
)

data class Connection(
    val serverA: ServerInfo,
    val serverB: ServerInfo,
)

data class ServerMapState(
    val mapSize: Coordinates,
    val serves: List<ServerInfo>,
    val connections: List<Connection>,
)
