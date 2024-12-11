package edu.agh.susgame.front.gui.components.game.components.elements.nodeinfo

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableIntState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import edu.agh.susgame.R
import edu.agh.susgame.front.gui.components.common.theme.PaddingS
import edu.agh.susgame.front.gui.components.common.theme.TextStyler

@Composable
fun Icons(
    resourceId: Int,
    cost: MutableIntState?,
    modifier: Modifier
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Image(
            painter = painterResource(id = resourceId),
            contentDescription = "Upgrade router",
            modifier = modifier
        )
        Row(
            modifier = Modifier
                .fillMaxSize()
                .weight(1f)
                .padding(
                    PaddingS
                ),
            horizontalArrangement = Arrangement.Absolute.Center,
            verticalAlignment = Alignment.CenterVertically
        )
        {
            if (cost != null) {
                Image(
                    painter = painterResource(id = R.drawable.token0),
                    contentDescription = "Fix router",
                    modifier = Modifier
                )
                Text(
                    text = cost.intValue.toString(),
                    style = TextStyler.TerminalM,
                    textAlign = TextAlign.Center
                )

            }
        }
    }
}