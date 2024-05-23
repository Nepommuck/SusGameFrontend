package edu.agh.susgame.front.model

import androidx.compose.ui.graphics.Color
import edu.agh.susgame.front.model.game.PlayerId

data class Player(
    val name: String,
    val color: Color = Color.Blue,
    var id: PlayerId = PlayerId(null),
) {
    fun resetId() {
        id = PlayerId(null)
    }
}
