package edu.agh.susgame.front.model.graph

import androidx.compose.ui.graphics.Color
import edu.agh.susgame.front.model.PlayerId

data class EdgeId(val value: Int)

class Edge(
    val id: EdgeId,
    val firstNodeId: NodeId,
    val secondNodeId: NodeId,
    var bandwidth: Int,
    var color: Color = Color.Black,
    val playersIdsUsingEdge : MutableSet<PlayerId> = mutableSetOf()
){
    fun addPlayer(playerId: PlayerId){
        playersIdsUsingEdge.add(playerId)
    }
    fun removePlayer(playerId: PlayerId){
        playersIdsUsingEdge.remove(playerId)
    }
}