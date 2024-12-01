package edu.agh.susgame.front.gui.components.menu.components.lobby.elements.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import edu.agh.susgame.front.gui.components.common.theme.PaddingXS
import edu.agh.susgame.front.gui.components.common.util.ColorProvider


@Composable
fun ColorMenuComp(
    onColorSelected: (Color) -> Unit
) {
    LazyVerticalGrid(
        columns = GridCells.Adaptive(minSize = 64.dp),
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Gray)
    ) {
        items(ColorProvider.colors) { color ->
            Box(
                modifier = Modifier
                    .size(64.dp)
                    .padding(PaddingXS)
                    .background(color, shape = RoundedCornerShape(20.dp))
                    .border(3.dp, Color.Black, shape = RoundedCornerShape(20.dp))
                    .clickable { onColorSelected(color) }
            )
        }
    }
}