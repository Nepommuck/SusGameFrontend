package edu.agh.susgame.front.providers.mock

import edu.agh.susgame.front.model.PlayerNickname
import edu.agh.susgame.front.model.game.AwaitingGame

import edu.agh.susgame.front.model.game.GameId
import edu.agh.susgame.front.providers.interfaces.AwaitingGamesProvider
import edu.agh.susgame.front.settings.Configuration

class MockAwaitingGamesProvider : AwaitingGamesProvider {
    // Current free id for new game
    private var freeGameId: Int = 0

    private var state = mutableListOf(
        AwaitingGame(
            id = GameId(freeGameId++),
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
        gameName: String,
        gamePIN: String,
        numOfPlayers: Int,
        gameTime: Int
    ): GameId {
        val gameId = GameId(freeGameId++)
        state.add(
            AwaitingGame(
                gameId,
                gameName,
                numOfPlayers,
                gameTime,
                gamePIN,
                emptyList(),
            )
        )
        return gameId
    }
}
