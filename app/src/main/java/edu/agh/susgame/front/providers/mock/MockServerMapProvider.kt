package edu.agh.susgame.front.providers.mock

import edu.agh.susgame.front.model.game.LobbyId
import edu.agh.susgame.front.model.graph.Edge
import edu.agh.susgame.front.model.graph.GameGraph
import edu.agh.susgame.front.model.graph.Host
import edu.agh.susgame.front.model.graph.Router
import edu.agh.susgame.front.model.graph.Server
import edu.agh.susgame.front.providers.interfaces.ServerMapProvider
import edu.agh.susgame.front.util.Coordinates
import java.util.concurrent.CompletableFuture

class MockServerMapProvider(mockDelayMs: Long? = null) : ServerMapProvider {
    private val delayMs = mockDelayMs ?: 0

    private val gameGraphState = GameGraph(
        mapSize = Coordinates(500, 200),
        nodes = mutableMapOf(),
        edges = mutableMapOf(),
    )

    init {
        createCustomMapState()
    }

    override fun getServerMapState(
        lobbyId: LobbyId,
    ): CompletableFuture<GameGraph> =
        CompletableFuture.supplyAsync {
            Thread.sleep(delayMs)
            gameGraphState
        }

    /**
     * This function is only for testing, it shows logic behind creating game map
     */
    private fun createCustomMapState() {


        gameGraphState.addNode(Router(0, "R1", Coordinates(150, 150), 30))
        gameGraphState.addNode(Router(1, "R2", Coordinates(250, 150), 30))
        gameGraphState.addNode(Router(2, "R3", Coordinates(200, 25), 30))

        gameGraphState.addNode(Host(3, "H1", Coordinates(100, 275), 0))
        gameGraphState.addNode(Host(4, "H2", Coordinates(200, 275), 1))
        gameGraphState.addNode(Host(5, "H3", Coordinates(350, 250), 2))

        gameGraphState.addNode(Server(6, "S1", Coordinates(75, 50), 300))



        gameGraphState.addEdge(Edge(0, 3, 0, 5))
        gameGraphState.addEdge(Edge(1, 4, 0, 5))
        gameGraphState.addEdge(Edge(2, 5, 1, 5))
        gameGraphState.addEdge(Edge(3, 0, 1, 5))
        gameGraphState.addEdge(Edge(4, 0, 2, 5))
        gameGraphState.addEdge(Edge(5, 0, 6, 5))
        gameGraphState.addEdge(Edge(6, 1, 2, 5))
        gameGraphState.addEdge(Edge(7, 2, 6, 5))

    }
}
