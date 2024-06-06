package edu.agh.susgame.front.providers.mock

import androidx.compose.ui.graphics.Color
import edu.agh.susgame.front.model.Player
import edu.agh.susgame.front.model.PlayerId
import edu.agh.susgame.front.model.PlayerNickname
import edu.agh.susgame.front.model.game.Lobby
import edu.agh.susgame.front.model.game.LobbyId
import edu.agh.susgame.front.providers.interfaces.LobbiesProvider
import edu.agh.susgame.front.providers.interfaces.LobbiesProvider.CreateNewGameResult
import edu.agh.susgame.front.settings.Configuration
import java.util.concurrent.CompletableFuture

class MockLobbiesProvider(mockDelayMs: Long? = null) : LobbiesProvider {
    private val delayMs = mockDelayMs ?: 0
    private var freeGameId: Int = 0
    private var freePlayerId: Int = 0
    private var currentLobbies: MutableMap<LobbyId, Lobby> = mutableMapOf()

    init {
        createCustomLobbies()
    }

    override fun getAll(): CompletableFuture<Map<LobbyId, Lobby>> =
        CompletableFuture.supplyAsync {
            Thread.sleep(delayMs)
            currentLobbies.toMap()
        }

    override fun getById(lobbyId: LobbyId): CompletableFuture<Lobby?> =
        this.getAll().thenApply { allGames ->
            allGames[lobbyId]
        }

    override fun createNewGame(
        gameName: String,
        gamePin: String,
        maxNumberOfPlayers: Int,
        gameTime: Int,
    ): CompletableFuture<CreateNewGameResult> =
        CompletableFuture.supplyAsync {
            Thread.sleep(delayMs)
            val lobbyId = LobbyId(freeGameId++)
            currentLobbies.putIfAbsent(
                lobbyId,
                Lobby(
                    lobbyId,
                    gameName,
                    maxNumberOfPlayers,
                    gameTime,
                )
            )
            CreateNewGameResult.Success(lobbyId)
        }

    /**
     * This method exists for a compatibility of `MockGameService` with `GameService` interface
     */
    fun hasPlayerJoinedLobby(lobbyId: LobbyId, playerNickname: PlayerNickname): Boolean =
        currentLobbies.values.toList()
            .find { it.id == lobbyId }
            ?.playersWaiting
            ?.values
            .orEmpty()
            .any { it.nickname == playerNickname }

    /**
     * This method exists for a compatibility of `MockGameService` with `GameService` interface
     */
    fun joinLobby(lobbyId: LobbyId, player: Player): CompletableFuture<Unit> =
        CompletableFuture.supplyAsync {
            Thread.sleep(delayMs)

            currentLobbies[lobbyId]?.let {
                currentLobbies[lobbyId] = lobbyWithPlayerAdded(it, player)
            }
        }

    /**
     * This method exists for a compatibility of `MockGameService` with `GameService` interface
     */
    fun leaveLobby(lobbyId: LobbyId, playerNickname: PlayerNickname): CompletableFuture<Unit> =
        CompletableFuture.supplyAsync {
            Thread.sleep(delayMs)

            currentLobbies[lobbyId]?.let {
                currentLobbies[lobbyId] = lobbyWithPlayerRemoved(it, playerNickname)
            }
        }

    /**
     * This function is only for testing, it shows logic behind creating new lobbies
     */
    private fun createCustomLobbies() {
        // game 1
        val player0 = Player(
            nickname = PlayerNickname("Player_0"),
            color = Color.Red
        )
        val player1 = Player(
            nickname = PlayerNickname("Player_1"),
            color = Color.Green
        )

        val lobbyIdValue1 = LobbyId(freeGameId++)
        currentLobbies.putIfAbsent(
            lobbyIdValue1, Lobby(
                id = lobbyIdValue1,
                name = "Gra dodana statycznie 1",
                maxNumOfPlayers = Configuration.MaxPlayersPerGame,
                gameTime = 10,
            )
        )

        this.joinLobby(lobbyIdValue1, player0)
        this.joinLobby(lobbyIdValue1, player1)

        // game 2
        val player2 = Player(
            nickname = PlayerNickname("Player_0"),
            color = Color.Red
        )
        val player3 = Player(
            nickname = PlayerNickname("Player_1"),
            color = Color.Green
        )

        val lobbyIdValue2 = LobbyId(freeGameId++)
        currentLobbies.putIfAbsent(
            lobbyIdValue2, Lobby(
                id = lobbyIdValue2,
                name = "Gra dodana statycznie 2",
                maxNumOfPlayers = 4,
                gameTime = 5,
            )
        )
        this.joinLobby(lobbyIdValue2, player2)
        this.joinLobby(lobbyIdValue2, player3)
    }

    private fun getFreePlayerId(): PlayerId {
        return PlayerId(freePlayerId++)
    }

    private fun lobbyWithPlayerAdded(lobby: Lobby, player: Player): Lobby {
        val playerId = player.id ?: getFreePlayerId()
        player.id = playerId

        return lobby.copy(
            playersWaiting = lobby.playersWaiting + (playerId to player),
        )
    }

    private fun lobbyWithPlayerRemoved(lobby: Lobby, playerNickname: PlayerNickname): Lobby {
        val foundPlayer = lobby.playersWaiting
            .filterValues { it.nickname == playerNickname }
            .toList()
            .firstOrNull()

        return when (foundPlayer) {
            null -> lobby
            else -> {
                val playerId = foundPlayer.first
                val player = foundPlayer.second

                player.resetId()
                lobby.copy(
                    playersWaiting = lobby.playersWaiting - playerId,
                )
            }
        }
    }
}
