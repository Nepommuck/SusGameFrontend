package edu.agh.susgame.front.providers.interfaces


import edu.agh.susgame.front.model.game.LobbyId
import edu.agh.susgame.front.model.graph.GameGraph
import java.util.concurrent.CompletableFuture

interface ServerMapProvider {
    fun getServerMapState(lobbyId: LobbyId): CompletableFuture<GameGraph>
}
