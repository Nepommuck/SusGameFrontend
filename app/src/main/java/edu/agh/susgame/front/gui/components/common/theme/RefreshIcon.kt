package edu.agh.susgame.front.gui.components.common.theme

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import edu.agh.susgame.R

@Composable
fun RefreshIcon(
    onRefreshClicked: () -> Unit,
) {
    Box(modifier = Modifier.fillMaxSize()) {
        Box(
            modifier = Modifier
                .requiredSize(120.dp)
                .padding(40.dp)
                .align(Alignment.TopEnd)
                .clickable {
                    onRefreshClicked()
                }
        ) {
            Image(
                painter = painterResource(R.drawable.refresh),
                contentDescription = "Loading Animation",
                modifier = Modifier
                    .fillMaxSize()
            )
        }
    }
}