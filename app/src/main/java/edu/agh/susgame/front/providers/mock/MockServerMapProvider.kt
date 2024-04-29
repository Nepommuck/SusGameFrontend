package edu.agh.susgame.front.providers.mock

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import edu.agh.susgame.front.model.Connection
import edu.agh.susgame.front.model.ServerInfo
import edu.agh.susgame.front.model.ServerMapState
import edu.agh.susgame.front.providers.interfaces.ServerMapProvider
import edu.agh.susgame.front.util.Coordinates

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