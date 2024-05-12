package edu.agh.susgame.front.model.game

import edu.agh.susgame.front.model.PlayerNickname

data class AwaitingGame(
    val id: GameId,
    val name: String,
    val maxNumOfPlayers: Int,
    val gameTime: Int,
    val gamePin: String, // probably not safe here, should be hidden in the future
    val playersWaiting: List<PlayerNickname>,

    ) {
    companion object {
        var freeId: Int =
            0 // current free id for new game, variable is static so it's shared among all instances
    }
}
