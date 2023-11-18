package movie.metropolis.app.screen.setup.component

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.staggeredgrid.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.text.style.*
import androidx.compose.ui.tooling.preview.*
import androidx.compose.ui.unit.*
import movie.style.layout.PreviewLayout
import movie.style.layout.alignForLargeScreen
import movie.style.modifier.glow
import movie.style.theme.Theme

@Composable
fun SetupContainerColumn(
    image: @Composable () -> Unit,
    title: @Composable () -> Unit,
    description: @Composable () -> Unit,
    cta: @Composable () -> Unit,
    modifier: Modifier = Modifier,
) = Column(
    modifier = modifier
        .statusBarsPadding()
        .navigationBarsPadding(),
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
    Column(
        modifier = Modifier.alignForLargeScreen(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
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
}

@Preview
@Composable
private fun SetupContainerColumnPreview() = PreviewLayout {
    SetupContainerColumn(
        image = {
            SetupPreviewLayout(
                count = 10,
                selectedItem = 0,
                state = rememberLazyStaggeredGridState()
            ) {
                Box(Modifier.glow(Theme.container.poster, alpha = .3f))
            }
        },
        title = { Text("Welcome back!") },
        description = { Text("Lorem ipsum dolor sit amet") },
        cta = {
            Button(onClick = { /*TODO*/ }) {
                Text("Continue")
            }
        }
    )
}