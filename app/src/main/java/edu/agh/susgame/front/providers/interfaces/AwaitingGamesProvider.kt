package edu.agh.susgame.front.providers.interfaces

import edu.agh.susgame.front.model.PlayerNickname
import edu.agh.susgame.front.model.game.AwaitingGame
import edu.agh.susgame.front.model.game.GameId

interface AwaitingGamesProvider {
    fun getAll(): MutableList<AwaitingGame>

    fun getById(gameId: GameId): AwaitingGame?

    fun join(id: GameId, playerNickname: PlayerNickname)

    fun leave(id: GameId, playerNickname: PlayerNickname)

    fun createNewGame(gameId: GameId, gameName: String, gamePIN: String, numOfPlayers: Int, gameTime: Int)
}
