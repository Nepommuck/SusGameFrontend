package edu.agh.susgame.front.providers.mock

import edu.agh.susgame.front.model.PlayerNickname
import edu.agh.susgame.front.model.game.AwaitingGame
import edu.agh.susgame.front.model.game.GameId
import edu.agh.susgame.front.providers.interfaces.AwaitingGamesProvider
import edu.agh.susgame.front.settings.Configuration
import java.util.concurrent.CompletableFuture

class MockAwaitingGamesProvider(mockDelayMs: Long? = null) : AwaitingGamesProvider {
    private val delayMs = mockDelayMs ?: 0

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
        AwaitingGame(
            id = GameId(freeGameId++),
            name = "Gra II",
            maxNumOfPlayers = 4,
            gameTime = 5,
            gamePin = null,
            playersWaiting = listOf("Jan", "Paweł", "II").map { PlayerNickname(it) },
        ),
    )

    override fun getAll(): CompletableFuture<List<AwaitingGame>> =
        CompletableFuture.supplyAsync {
            Thread.sleep(delayMs)
            state.toList()
        }

    override fun getById(gameId: GameId): CompletableFuture<AwaitingGame?> =
        this.getAll().thenApply { allGames ->
            allGames.find { it.id == gameId }
        }

    override fun join(id: GameId, playerNickname: PlayerNickname): CompletableFuture<Unit> =
        CompletableFuture.supplyAsync {
            Thread.sleep(delayMs)
            state = state.map {
                if (it.id == id)
                    it.copy(playersWaiting = it.playersWaiting + playerNickname)
                else
                    it
            }.toMutableList()
        }

    override fun leave(id: GameId, playerNickname: PlayerNickname): CompletableFuture<Unit> =
        CompletableFuture.supplyAsync {
            Thread.sleep(delayMs)
            state = state.map { game ->
                if (game.id == id)
                    game.copy(playersWaiting = game.playersWaiting.filter { it != playerNickname })
                else
                    game
            }.toMutableList()
        }

    override fun createNewGame(
        gameName: String,
        gamePin: String,
        numOfPlayers: Int,
        gameTime: Int,
    ): CompletableFuture<GameId> =
        CompletableFuture.supplyAsync {
            Thread.sleep(delayMs)
            val gameId = GameId(freeGameId++)
            state.add(
                AwaitingGame(
                    gameId,
                    gameName,
                    numOfPlayers,
                    gameTime,
                    gamePin,
                    playersWaiting = emptyList(),
                )
            )
            gameId
        }
}
