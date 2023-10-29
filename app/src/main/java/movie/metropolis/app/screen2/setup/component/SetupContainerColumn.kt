package movie.metropolis.app.screen2.setup.component

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.graphics.*
import androidx.compose.ui.text.style.*
import androidx.compose.ui.tooling.preview.*
import androidx.compose.ui.unit.*
import movie.style.AppButton
import movie.style.layout.PreviewLayout
import movie.style.modifier.clipWithGlow
import movie.style.theme.Theme

@Composable
fun SetupContainerColumn(
    image: @Composable () -> Unit,
    title: @Composable () -> Unit,
    description: @Composable () -> Unit,
    cta: @Composable () -> Unit,
    modifier: Modifier = Modifier,
) = Column(
    modifier = modifier,
    horizontalAlignment = Alignment.CenterHorizontally,
    verticalArrangement = Arrangement.spacedBy(16.dp)
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .weight(1f)
    ) {
        image()
    }
    ProvideTextStyle(Theme.textStyle.title.copy(textAlign = TextAlign.Center)) {
        title()
    }
    ProvideTextStyle(Theme.textStyle.body.copy(textAlign = TextAlign.Center)) {
        description()
    }
    Box(
        modifier = Modifier.fillMaxWidth(),
        propagateMinConstraints = true
    ) {
        cta()
    }
}

@Preview
@Composable
private fun SetupContainerColumnPreview() = PreviewLayout {
    SetupContainerColumn(
        image = {
            SetupPreviewLayout(count = 10) { index, selected ->
                Box(
                    Modifier
                        .clipWithGlow(Theme.container.poster, Color.Yellow, alpha = .3f)
                )
            }
        },
        title = { Text("Welcome back!") },
        description = { Text("Lorem ipsum dolor sit amet") },
        cta = {
            AppButton(onClick = { /*TODO*/ }) {
                Text("Continue")
            }
        }
    )
}