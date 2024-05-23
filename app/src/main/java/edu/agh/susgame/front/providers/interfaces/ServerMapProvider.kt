package edu.agh.susgame.front.providers.interfaces


import edu.agh.susgame.front.model.game.LobbyId
import edu.agh.susgame.front.model.graph.Graph
import java.util.concurrent.CompletableFuture

interface ServerMapProvider {
    fun getServerMapState(lobbyId: LobbyId): CompletableFuture<Graph>
    fun createCustomMapState()// this function is only for testing, it shows logic behind creating game map
}
