package movie.metropolis.app.screen.share

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.draw.*
import androidx.compose.ui.graphics.*
import androidx.compose.ui.res.*
import androidx.compose.ui.text.style.*
import androidx.compose.ui.tooling.preview.*
import movie.metropolis.app.R
import movie.metropolis.app.screen.share.component.Background
import movie.style.theme.Theme

@Composable
fun ShareScreen() {
    Scaffold { padding ->
        Background(count = 9) {
            Image(
                modifier = Modifier
                    .fillMaxSize()
                    .alpha(.2f),
                painter = painterResource(id = R.drawable.ic_share),
                contentDescription = null,
                colorFilter = ColorFilter.tint(
                    when (it % 3) {
                        1 -> Theme.color.container.primary
                        2 -> Theme.color.container.tertiary
                        else -> Theme.color.container.secondary
                    }

                )
            )
        }
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = stringResource(R.string.share_method),
                style = Theme.textStyle.title,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun Preview() {
    Theme {
        ShareScreen()
    }
}