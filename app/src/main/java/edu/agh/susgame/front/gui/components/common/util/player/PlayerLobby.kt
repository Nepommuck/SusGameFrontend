package edu.agh.susgame.front.gui.components.common.util.player

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import edu.agh.susgame.dto.rest.model.PlayerId
import edu.agh.susgame.dto.rest.model.PlayerNickname

data class PlayerLobby(
    var name: PlayerNickname = PlayerNickname("default"),
    var status: MutableState<PlayerStatus> = mutableStateOf(PlayerStatus.NOT_READY),
    var id: PlayerId? = null
)