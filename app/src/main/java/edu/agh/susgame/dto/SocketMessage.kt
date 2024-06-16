// WARNING: THIS FILE WAS CLONED AUTOMATICALLY FROM 'SusGameDTO' GITHUB REPOSITORY
// IT SHOULD NOT BE EDITED IN ANY WAY
// IN ORDER TO CHANGE THIS DTO, COMMIT TO 'SusGameDTO' GITHUB REPOSITORY
// IN ORDER TO UPDATE THIS FILE TO NEWEST VERSION, RUN 'scripts/update-DTO.sh'

package edu.agh.susgame.dto

import kotlinx.serialization.Serializable


@Serializable
sealed class SocketMessage {
    // TODO CDM-72 Pretty much everything
    @Serializable
    data object GameState : SocketMessage()

    @Serializable
    data class SimpleMessage(
        val authorNickname: String,
        val message: String,
    ) : SocketMessage()
}
