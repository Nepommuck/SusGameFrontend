package edu.agh.susgame.front.service.mock

import androidx.compose.ui.graphics.Color
import edu.agh.susgame.dto.rest.model.GameMapDTO
import edu.agh.susgame.dto.rest.model.Lobby
import edu.agh.susgame.dto.rest.model.LobbyId
import edu.agh.susgame.dto.rest.model.PlayerId
import edu.agh.susgame.dto.rest.model.PlayerNickname
import edu.agh.susgame.dto.rest.model.PlayerREST
import edu.agh.susgame.front.config.AppConfig
import edu.agh.susgame.front.managers.LobbyManager
import edu.agh.susgame.front.service.interfaces.CreateNewGameResult
import edu.agh.susgame.front.service.interfaces.LobbyService
import java.util.concurrent.CompletableFuture

// IGNORE THIS, ITS GONNA BE DELETED
class MockLobbyService(mockDelayMs: Long? = null) : LobbyService {
    private val delayMs = mockDelayMs ?: 0
    private var freeGameId: Int = 0
    private var currentLobbies: MutableMap<LobbyId, Lobby> = mutableMapOf()

    init {
        createCustomLobbies()
    }

    override var lobbyManager: LobbyManager? = null

    override fun addLobbyManager(lobbyManager: LobbyManager) {
        this.lobbyManager = lobbyManager
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

    override fun getGameMap(lobbyId: LobbyId): CompletableFuture<GameMapDTO?> =
        // TODO Return some mock map
        CompletableFuture.supplyAsync { null }

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
                    playersWaiting = emptyList(),
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
            .orEmpty()
            .any { it.nickname == playerNickname }

    /**
     * This method exists for a compatibility of `MockGameService` with `GameService` interface
     */
    fun joinLobby(lobbyId: LobbyId, player: PlayerREST): CompletableFuture<Unit> {
        lobbyManager?.addPlayerRest(player)
        println(lobbyManager?.playersMap)
        return CompletableFuture.supplyAsync {
            Thread.sleep(delayMs)

            currentLobbies[lobbyId]?.let {
                currentLobbies[lobbyId] = lobbyWithPlayerAdded(it, player)
            }
        }
    }

    /**
     * This method exists for a compatibility of `MockGameService` with `GameService` interface
     */
    fun leaveLobby(lobbyId: LobbyId, playerNickname: PlayerNickname): CompletableFuture<Unit> {

        return CompletableFuture.supplyAsync {
            Thread.sleep(delayMs)

            currentLobbies[lobbyId]?.let {
                currentLobbies[lobbyId] = lobbyWithPlayerRemoved(it, playerNickname)
            }
        }

    }

    /**
     * This function is only for testing, it shows logic behind creating new lobbies
     */
    private fun createCustomLobbies() {
        // game 1
        val player0 = PlayerREST(
            nickname = PlayerNickname("Player_0"),
            id = PlayerId(0),
            colorHex = Color.Red.value.toLong(),
        )
        val player1 = PlayerREST(
            nickname = PlayerNickname("Player_1"),
            id = PlayerId(1),
            colorHex = Color.Green.value.toLong(),
        )

        val lobbyIdValue1 = LobbyId(freeGameId++)
        currentLobbies.putIfAbsent(
            lobbyIdValue1, Lobby(
                id = lobbyIdValue1,
                name = "Gra dodana statycznie 1",
                maxNumOfPlayers = AppConfig.gameConfig.playersPerGame.max,
                gameTime = 10,
                playersWaiting = emptyList(),
            )
        )

        this.joinLobby(lobbyIdValue1, player0)
        this.joinLobby(lobbyIdValue1, player1)

        // game 2
        val player2 = PlayerREST(
            nickname = PlayerNickname("Player_0"),
            id = PlayerId(0),
            colorHex = Color.Red.value.toLong(),
        )
        val player3 = PlayerREST(
            nickname = PlayerNickname("Player_1"),
            id = PlayerId(1),
            colorHex = Color.Green.value.toLong(),
        )

        val lobbyIdValue2 = LobbyId(freeGameId++)
        currentLobbies.putIfAbsent(
            lobbyIdValue2,
            Lobby(
                id = lobbyIdValue2,
                name = "Gra dodana statycznie 2",
                maxNumOfPlayers = 4,
                gameTime = 5,
                playersWaiting = listOf(
                    PlayerREST(
                        nickname = PlayerNickname("Nonexistent-PlayerREST"),
                        id = PlayerId(99),
                        colorHex = 0xFF0000,
                    ),
                ),
            ),
        )
        this.joinLobby(lobbyIdValue2, player2)
        this.joinLobby(lobbyIdValue2, player3)
    }

    private fun lobbyWithPlayerAdded(lobby: Lobby, player: PlayerREST): Lobby {
        return lobby.copy(
            playersWaiting = lobby.playersWaiting + player,
        )
    }

    private fun lobbyWithPlayerRemoved(lobby: Lobby, playerNickname: PlayerNickname): Lobby {
        val foundPlayer = lobby.playersWaiting
            .filter { it.nickname == playerNickname }
            .toList()
            .firstOrNull()

        return when (foundPlayer) {
            null -> lobby
            else -> {
                lobby.copy(
                    playersWaiting = lobby.playersWaiting - foundPlayer,
                )
            }
        }
    }
}
