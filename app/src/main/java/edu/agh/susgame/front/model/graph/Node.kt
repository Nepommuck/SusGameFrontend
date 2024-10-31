package edu.agh.susgame.front.model.graph

import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import edu.agh.susgame.dto.rest.model.PlayerId
import edu.agh.susgame.front.Translation
import edu.agh.susgame.front.ui.components.common.util.Coordinates

data class NodeId(val value: Int)
abstract class Node(
    val id: NodeId,
    val name: String,
    val position: Coordinates,
) {
    abstract fun getInfo(): String // TODO fow now it's in string, later on this should be improved to something more accurate
}







