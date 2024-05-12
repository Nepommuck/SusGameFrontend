package edu.agh.susgame.front.providers.mock

import edu.agh.susgame.front.model.PlayerNickname
import edu.agh.susgame.front.model.game.AwaitingGame
import edu.agh.susgame.front.model.game.AwaitingGame.Companion.freeId
import edu.agh.susgame.front.model.game.GameId
import edu.agh.susgame.front.providers.interfaces.AwaitingGamesProvider
import edu.agh.susgame.front.settings.Configuration
class MockAwaitingGamesProvider : AwaitingGamesProvider {
    private var state = mutableListOf(
        AwaitingGame(
            id = GameId(freeId++),
            name = "Gra dodana statycznie",
            maxNumOfPlayers = Configuration.MaxPlayersPerGame,
            gameTime = 10,
            gamePin = "game pin",
            playersWaiting = listOf("John", "Terry").map { PlayerNickname(it) },
        ),
    )

    override fun getAll(): MutableList<AwaitingGame> = state

    override fun getById(gameId: GameId): AwaitingGame? =
        this.getAll()
            .find { it.id == gameId }

    override fun join(id: GameId, playerNickname: PlayerNickname) {
        state = state.map {
            if (it.id == id)
                it.copy(playersWaiting = it.playersWaiting + playerNickname)
            else
                it
        }.toMutableList()
    }

    override fun leave(id: GameId, playerNickname: PlayerNickname) {
        state = state.map { game ->
            if (game.id == id)
                game.copy(playersWaiting = game.playersWaiting.filter { it != playerNickname })
            else
                game
        }.toMutableList()
    }

    override fun createNewGame(
        gameId: GameId,
        gameName: String,
        gamePIN: String,
        numOfPlayers: Int,
        gameTime: Int
    ) {
        state.add(AwaitingGame(gameId,gameName,numOfPlayers,gameTime,gamePIN, listOf<String>().map { PlayerNickname(it) }))
    }
}