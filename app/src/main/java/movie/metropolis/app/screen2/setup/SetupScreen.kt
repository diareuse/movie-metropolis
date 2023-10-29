@file:OptIn(ExperimentalMaterial3Api::class)

package movie.metropolis.app.screen2.setup

import android.content.res.Configuration.UI_MODE_NIGHT_NO
import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.draw.*
import androidx.compose.ui.tooling.preview.*
import androidx.compose.ui.unit.*
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList
import movie.metropolis.app.screen2.setup.component.SetupContainerColumn
import movie.metropolis.app.screen2.setup.component.SetupPreviewLayout
import movie.style.AppButton
import movie.style.Image
import movie.style.layout.PreviewLayout
import movie.style.modifier.clipWithGlow
import movie.style.rememberPaletteImageState
import movie.style.theme.Theme

@Composable
fun SetupScreen(
    state: SetupState,
    posters: ImmutableList<String>,
    onStateChange: (SetupState) -> Unit,
    modifier: Modifier = Modifier
) = Scaffold(
    modifier = modifier,
    topBar = {},
    bottomBar = {}
) { padding ->
    val modifier = Modifier.padding(padding)
    AnimatedContent(targetState = state) { state ->
        when (state) {
            SetupState.Initial -> SetupInitialContent(
                modifier = modifier,
                posters = posters,
                onContinueClick = { onStateChange(SetupState.RegionSelection) }
            )

            SetupState.RegionSelection -> SetupRegionSelectionContent(modifier = modifier)
        }
    }
}

enum class SetupState {
    Initial, RegionSelection
}

@Composable
private fun SetupInitialContent(
    posters: ImmutableList<String>,
    onContinueClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    SetupContainerColumn(
        modifier = modifier,
        image = {
            SetupPreviewLayout(
                count = posters.size,
                contentPadding = PaddingValues(24.dp)
            ) { it, selected ->
                val state = rememberPaletteImageState(url = posters[it])
                val elevation by animateDpAsState(if (selected) 16.dp else 0.dp, tween(700))
                val scale by animateFloatAsState(if (selected) 1.5f else 1f, tween(700))
                val color = state.palette.color
                Image(
                    modifier = Modifier
                        .scale(scale)
                        .clipWithGlow(
                            shape = Theme.container.poster,
                            backgroundColor = Theme.color.container.background
                        )
                        .shadow(elevation, ambientColor = color, spotColor = color),
                    state = state
                )
            }
        },
        title = {
            Text(
                modifier = Modifier.padding(horizontal = 24.dp),
                text = "Welcome to Movie Metropolis!"
            )
        },
        description = {
            Text(
                modifier = Modifier.padding(horizontal = 24.dp),
                text = "Movie Metropolis is a sister application for browsing, managing and buying tickets in Cinema City theaters and multiplexes.\n\nFirstly we need you to choose in which region are you going to use the app so we can offer you personalized results."
            )
        },
        cta = {
            AppButton(modifier = Modifier.padding(24.dp), onClick = onContinueClick) {
                Text("Continue")
            }
        }
    )
}

@Composable
private fun SetupRegionSelectionContent(modifier: Modifier = Modifier) {

}

@Preview(showBackground = true, uiMode = UI_MODE_NIGHT_YES)
@Preview(showBackground = true, uiMode = UI_MODE_NIGHT_NO)
@Composable
private fun SetupScreenPreview(
    @PreviewParameter(SetupScreenParameter::class)
    parameter: SetupScreenParameter.Data
) = PreviewLayout {
    SetupScreen(
        state = parameter.state,
        posters = List(20) { "" }.toImmutableList(),
        onStateChange = {}
    )
}

private class SetupScreenParameter : PreviewParameterProvider<SetupScreenParameter.Data> {
    override val values = sequence {
        yield(Data(SetupState.Initial))
        yield(Data(SetupState.RegionSelection))
    }

    class Data(val state: SetupState)
}