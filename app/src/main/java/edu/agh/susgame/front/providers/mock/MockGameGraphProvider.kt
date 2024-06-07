package edu.agh.susgame.front.providers.mock

import edu.agh.susgame.front.model.game.LobbyId
import edu.agh.susgame.front.model.graph.Edge
import edu.agh.susgame.front.model.graph.EdgeId
import edu.agh.susgame.front.model.graph.GameGraph
import edu.agh.susgame.front.model.graph.Host
import edu.agh.susgame.front.model.graph.NodeId
import edu.agh.susgame.front.model.graph.Router
import edu.agh.susgame.front.model.graph.Server
import edu.agh.susgame.front.providers.interfaces.GameGraphProvider
import edu.agh.susgame.front.util.Coordinates
import java.util.concurrent.CompletableFuture

class MockGameGraphProvider(mockDelayMs: Long? = null) : GameGraphProvider {
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

        listOf(
            Router(NodeId(0), "R1", Coordinates(150, 150), 30),
            Router(NodeId(1), "R2", Coordinates(250, 150), 30),
            Router(NodeId(2), "R3", Coordinates(200, 25), 30),

            Host(NodeId(3), "H1", Coordinates(100, 275), 0),
            Host(NodeId(4), "H2", Coordinates(200, 275), 1),
            Host(NodeId(5), "H3", Coordinates(350, 250), 2),

            Server(NodeId(6), "S1", Coordinates(75, 50), 300),
        ).forEach {
            gameGraphState.addNode(it)
        }

        listOf(
            Edge(EdgeId(0), NodeId(3), NodeId(0), 5),
            Edge(EdgeId(1), NodeId(4), NodeId(0), 5),
            Edge(EdgeId(2), NodeId(1), NodeId(5), 5),
            Edge(EdgeId(3), NodeId(0), NodeId(1), 5),
            Edge(EdgeId(4), NodeId(0), NodeId(2), 5),
            Edge(EdgeId(5), NodeId(0), NodeId(6), 5),
            Edge(EdgeId(6), NodeId(1), NodeId(2), 5),
            Edge(EdgeId(7), NodeId(2), NodeId(6), 5),
        ).forEach {
            gameGraphState.addEdge(it)
        }


    }
}
