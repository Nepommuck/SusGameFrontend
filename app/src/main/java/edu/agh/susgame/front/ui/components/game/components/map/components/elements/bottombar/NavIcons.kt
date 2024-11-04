package edu.agh.susgame.front.ui.components.game.components.map.components.elements.bottombar

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import edu.agh.susgame.R
import edu.agh.susgame.front.ui.components.common.theme.PaddingXS

private val sizeDp = 50.dp

@Composable
fun NavIcons(isComputerVisible: Boolean, setComputerViewVisibility: (Boolean) -> Unit) {

    Row(
        modifier = Modifier
            .fillMaxSize()
            .wrapContentSize(align = Alignment.BottomStart)
    ) {

        Image(painter = painterResource(id = R.drawable.network),
            contentDescription = null,
            modifier = Modifier
                .size(sizeDp)
                .padding(PaddingXS)
                .graphicsLayer { alpha = if (!isComputerVisible) 1f else 0.5f }
                .clickable {
                    setComputerViewVisibility(false)
                }
        )

        Image(painter = painterResource(id = R.drawable.button_console),
            contentDescription = null,
            modifier = Modifier
                .size(sizeDp)
                .padding(PaddingXS)
                .graphicsLayer { alpha = if (isComputerVisible) 1f else 0.5f }
                .clickable {
                    setComputerViewVisibility(true)
                }
        )
    }
}