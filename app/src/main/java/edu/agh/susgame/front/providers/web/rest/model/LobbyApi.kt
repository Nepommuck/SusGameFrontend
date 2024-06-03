package edu.agh.susgame.front.providers.web.rest.model

data class LobbyApi(
    val id: Int,
    val name: String,
    val maxNumberOfPlayers: Int,
    val players: List<String>,
)
