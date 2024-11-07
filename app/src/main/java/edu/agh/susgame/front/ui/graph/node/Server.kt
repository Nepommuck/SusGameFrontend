package edu.agh.susgame.front.ui.graph.node

import androidx.compose.runtime.MutableIntState
import androidx.compose.runtime.mutableIntStateOf
import edu.agh.susgame.front.Translation
import edu.agh.susgame.front.ui.components.common.util.Coordinates

class Server(
    id: NodeId,
    name: String,
    position: Coordinates,
    val packetsToWin: Int,
    var packetsReceived: MutableIntState = mutableIntStateOf(0)
) : Node(id, name, position) {

    override fun getInfo(): String {
        return """
            ${Translation.Game.SERVER}
            ${Translation.Game.PACKETS_TO_WIN}: $packetsToWin
            ${Translation.Game.PACKETS_RECEIVED}: ${packetsReceived.intValue}
        """.trimIndent()
    }

    fun setReceived(n: Int) {
        packetsReceived.intValue += n
    }
}