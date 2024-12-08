package edu.agh.susgame.front.gui.components.common.util.player


import androidx.compose.runtime.MutableIntState
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.ui.graphics.Color
import edu.agh.susgame.dto.rest.model.PlayerId
import edu.agh.susgame.dto.rest.model.PlayerNickname

class PlayerLobby(
    var name: PlayerNickname,
    var id: PlayerId,
    val color: MutableState<Color>,
    val status: MutableState<PlayerStatus>
) {
    val tokens: MutableIntState = mutableIntStateOf(0)
}
