package edu.agh.susgame.front.service.interfaces


import edu.agh.susgame.front.model.game.LobbyId
import edu.agh.susgame.front.model.graph.GameGraph
import java.util.concurrent.CompletableFuture

/**
 * This Provider will soon be deleted. Map state will be served by `GameService`
 **/
interface ServerMapProvider {
    fun getServerMapState(lobbyId: LobbyId): CompletableFuture<GameGraph>
}
