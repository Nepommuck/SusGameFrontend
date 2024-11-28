package edu.agh.susgame.front.gui.components.common.util.player


import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.Color
import edu.agh.susgame.dto.rest.model.PlayerId
import edu.agh.susgame.dto.rest.model.PlayerNickname

class PlayerLobby(
    var name: PlayerNickname = PlayerNickname("default"),
    var id: PlayerId = PlayerId(2137)
) {
    val status: MutableState<PlayerStatus> = mutableStateOf(PlayerStatus.NOT_READY)
    val color: MutableState<Color> = mutableStateOf(Color.Red)
}
