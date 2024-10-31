package edu.agh.susgame.front.model.graph.nodes

import androidx.compose.runtime.mutableIntStateOf
import edu.agh.susgame.front.Translation
import edu.agh.susgame.front.model.graph.Node
import edu.agh.susgame.front.model.graph.NodeId
import edu.agh.susgame.front.ui.components.common.util.Coordinates

class Server(
    id: NodeId,
    name: String,
    position: Coordinates,
    private val packetsToWin: Int,
) : Node(id, name, position) {
    var packetsReceived = mutableIntStateOf(0)
    override fun getInfo(): String {
        return """
            ${Translation.Game.SERVER}
            ${Translation.Game.PACKETS_TO_WIN}: $packetsToWin
            ${Translation.Game.PACKETS_RECEIVED}: ${packetsReceived.intValue}
        """.trimIndent()
    }
    fun getPacketsToWin() : Int {
        return packetsToWin
    }
    fun setReceived(n : Int){
        packetsReceived.intValue+=n;
    }
}