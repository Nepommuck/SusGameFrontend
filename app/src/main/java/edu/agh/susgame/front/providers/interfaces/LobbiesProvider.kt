package edu.agh.susgame.front.providers.interfaces

import edu.agh.susgame.front.model.Player
import edu.agh.susgame.front.model.game.Lobby
import edu.agh.susgame.front.model.game.LobbyId
import edu.agh.susgame.front.model.game.PlayerId
import java.util.concurrent.CompletableFuture

interface LobbiesProvider {
    fun getAll(): CompletableFuture<MutableMap<LobbyId, Lobby>>

    fun getById(lobbyId: LobbyId): CompletableFuture<Lobby?>

    fun join(lobbyId: LobbyId, player: Player): CompletableFuture<Unit>

    fun leave(lobbyId: LobbyId, playerId: PlayerId): CompletableFuture<Unit>

    fun createNewGame(
        gameName: String,
        gamePin: String,
        numOfPlayers: Int,
        gameTime: Int,
    ): CompletableFuture<LobbyId>

    fun createCustomLobbies() // this function is only for testing, it shows logic behind creating new lobbies
}
