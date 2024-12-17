package edu.agh.susgame.front.gui.components.common.graph.edge

import androidx.compose.runtime.MutableIntState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.ui.graphics.Color
import edu.agh.susgame.dto.rest.model.PlayerId
import edu.agh.susgame.front.gui.components.common.graph.node.NodeId

data class EdgeId(val value: Int)

class Edge(
    val id: EdgeId,
    val firstNodeId: NodeId,
    val secondNodeId: NodeId,
    var bandwidth: Int,
) {
    var color: Color = Color.Black
    var playersIdsUsingEdge = mutableStateListOf<PlayerId>()
    val packetsTransported: MutableIntState = mutableIntStateOf(0)
    val upgradeCost: MutableIntState = mutableIntStateOf(0)

    fun addPlayer(playerId: PlayerId) {
        if (!playersIdsUsingEdge.contains(playerId)) {
            playersIdsUsingEdge.add(playerId)
        }
    }

    fun removePlayer(playerId: PlayerId) {
        playersIdsUsingEdge.remove(playerId)
    }

}