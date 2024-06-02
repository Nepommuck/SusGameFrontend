package edu.agh.susgame.front.model

import androidx.compose.ui.graphics.Color

data class PlayerId(val value: Int)
class Player(
    val name: String,
    val color: Color = Color.Blue,
    var id: PlayerId? = null,
) {
    fun resetId() {
        id = null
    }
}
