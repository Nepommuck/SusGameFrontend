package edu.agh.susgame.front.map

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import edu.agh.susgame.front.util.Coordinates

interface ServerMapProvider {
    fun getServerMapState(): MutableState<ServerMapState>
}

class MockServerMapProvider : ServerMapProvider {
    private val server1 = ServerInfo("S 01", Coordinates(10, 20))
    private val server2 = ServerInfo("S 02", Coordinates(460, 50))
    private val server3 = ServerInfo("S 03", Coordinates(120, 130))

    private val serverMapState = mutableStateOf(
        ServerMapState(
            mapSize = Coordinates(500, 200),
            serves = listOf(server1, server2, server3),
            connections = listOf(
                Connection(server1, server2),
            ),
        )
    )

    override fun getServerMapState(): MutableState<ServerMapState> {
        return serverMapState
    }
}