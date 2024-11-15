package edu.agh.susgame.front.gui.components.common.util.player

import androidx.compose.runtime.MutableState
import edu.agh.susgame.dto.rest.model.PlayerNickname

data class PlayerLobby(val name: PlayerNickname, val status: MutableState<PlayerStatus>)
