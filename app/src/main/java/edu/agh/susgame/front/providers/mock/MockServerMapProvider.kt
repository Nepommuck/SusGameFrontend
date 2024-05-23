package edu.agh.susgame.front.providers.mock

import edu.agh.susgame.front.model.game.LobbyId
import edu.agh.susgame.front.model.graph.Edge
import edu.agh.susgame.front.model.graph.Graph
import edu.agh.susgame.front.model.graph.Host
import edu.agh.susgame.front.model.graph.Router
import edu.agh.susgame.front.model.graph.Server
import edu.agh.susgame.front.providers.interfaces.ServerMapProvider
import edu.agh.susgame.front.util.Coordinates
import java.util.concurrent.CompletableFuture

class MockServerMapProvider(mockDelayMs: Long? = null) : ServerMapProvider {
    private val delayMs = mockDelayMs ?: 0

    private val serverMapState = Graph(
        mapSize = Coordinates(500, 200),
        nodeMap = mutableMapOf(),
        edgeMap = mutableMapOf(),
    )

    override fun getServerMapState(
        lobbyId: LobbyId,
    ): CompletableFuture<Graph> =
        CompletableFuture.supplyAsync {
            Thread.sleep(delayMs)
            serverMapState
        }

    override fun createCustomMapState() {
        val router1 = Router(0, "R1", Coordinates(10, 330), 30)
        val host1 = Host(1, "H1", Coordinates(460, 50), 3)
        val server1 = Server(2, "S1", Coordinates(120, 130), 300)

        val routerHostEdge = Edge(0, 0, 1, 5)
        val hostServerEdge = Edge(1, 1, 2, 3)
        val serverRouterEdge = Edge(1, 1, 2, 4)

        serverMapState.addNode(router1)
        serverMapState.addNode(host1)
        serverMapState.addNode(server1)

        serverMapState.addEdge(routerHostEdge)
        serverMapState.addEdge(hostServerEdge)
        serverMapState.addEdge(serverRouterEdge)
    }
}
