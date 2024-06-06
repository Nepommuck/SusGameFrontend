package edu.agh.susgame.front.model

import androidx.compose.ui.graphics.Color

data class PlayerId(val value: Int)
data class PlayerNickname(val value: String)

class Player(
    val nickname: PlayerNickname,
    val color: Color = Color.Blue,
    var id: PlayerId? = null,
) {
    fun resetId() {
        id = null
    }
}
