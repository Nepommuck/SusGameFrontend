package edu.agh.susgame.front.providers.interfaces

import edu.agh.susgame.front.model.PlayerNickname
import edu.agh.susgame.front.model.game.AwaitingGame
import edu.agh.susgame.front.model.game.GameId
import java.util.concurrent.CompletableFuture

interface AwaitingGamesProvider {
    fun getAll(): CompletableFuture<List<AwaitingGame>>

    fun getById(gameId: GameId): CompletableFuture<AwaitingGame?>

    fun join(id: GameId, playerNickname: PlayerNickname): CompletableFuture<Unit>

    fun leave(id: GameId, playerNickname: PlayerNickname): CompletableFuture<Unit>

    fun createNewGame(
        gameName: String,
        gamePIN: String,
        numOfPlayers: Int,
        gameTime: Int,
    ): CompletableFuture<GameId>
}
