package edu.agh.susgame.front.service.web.rest.games.model

import edu.agh.susgame.front.model.game.LobbyId
import edu.agh.susgame.front.service.web.rest.model.LobbyApi


sealed class GetAllGamesApiResult {
    data class Success(val lobbies: List<LobbyApi>) : GetAllGamesApiResult()
    data object Error : GetAllGamesApiResult()
}

sealed class GetGameApiResult {
    data class Success(val lobby: LobbyApi) : GetGameApiResult()
    data object DoesNotExist : GetGameApiResult()
    data object OtherError : GetGameApiResult()
}

sealed class CreateGameApiResult {
    data class Success(val createdLobbyId: LobbyId) : CreateGameApiResult()
    data object NameAlreadyExists : CreateGameApiResult()
    data object OtherError : CreateGameApiResult()
}
