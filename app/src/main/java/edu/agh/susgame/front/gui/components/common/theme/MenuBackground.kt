package edu.agh.susgame.front.gui.components.common.theme

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import edu.agh.susgame.R

@Composable
fun MenuBackground(
) {
    Image(
        painter = painterResource(id = R.drawable.menu_background),
        contentDescription = null,
        modifier = Modifier
            .fillMaxSize(),
        contentScale = ContentScale.Crop
    )
}