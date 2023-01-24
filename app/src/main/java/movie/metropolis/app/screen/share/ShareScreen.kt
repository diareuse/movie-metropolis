package movie.metropolis.app.screen.share

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import movie.metropolis.app.R
import movie.metropolis.app.screen.setup.Background
import movie.style.theme.Theme

@OptIn(ExperimentalMaterial3Api::class)
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