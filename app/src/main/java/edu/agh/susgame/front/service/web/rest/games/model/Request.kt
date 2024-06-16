package edu.agh.susgame.front.service.web.rest.games.model

data class GameCreationRequest(
    val gameName: String,
    val maxNumberOfPlayers: Int = 4,
    val gamePin: String? = null,
)
