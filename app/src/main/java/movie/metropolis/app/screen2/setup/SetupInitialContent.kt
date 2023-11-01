@file:OptIn(ExperimentalMaterial3Api::class)

package movie.metropolis.app.screen2.setup

import android.content.res.Configuration
import androidx.compose.animation.core.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.staggeredgrid.*
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
import movie.metropolis.app.util.rememberVisibleItemAsState
import movie.style.AppButton
import movie.style.BackgroundImage
import movie.style.Image
import movie.style.layout.PreviewLayout
import movie.style.modifier.glow
import movie.style.modifier.surface
import movie.style.rememberImageState
import movie.style.rememberPaletteImageState
import movie.style.theme.Theme

@Composable
fun SetupInitialContent(
    posters: ImmutableList<String>,
    onContinueClick: () -> Unit,
    modifier: Modifier = Modifier
) = Box(
    modifier = modifier,
    propagateMinConstraints = true
) {
    val state = rememberLazyStaggeredGridState()
    val selectedItem by state.rememberVisibleItemAsState()
    BackgroundImage(
        state = rememberImageState(url = posters.getOrNull(selectedItem))
    )
    SetupContainerColumn(
        image = {
            SetupPreviewLayout(
                count = posters.size,
                contentPadding = PaddingValues(24.dp),
                selectedItem = selectedItem,
                state = state
            ) {
                val selected = it == selectedItem
                val state = rememberPaletteImageState(url = posters[it])
                val elevation by animateDpAsState(if (selected) 16.dp else 0.dp, tween(700))
                val scale by animateFloatAsState(if (selected) 1.5f else 1f, tween(700))
                val color = state.palette.color
                Image(
                    modifier = Modifier
                        .scale(scale)
                        .surface(
                            shape = Theme.container.poster,
                            color = Theme.color.container.background,
                            elevation = elevation,
                            shadowColor = color
                        )
                        .glow(Theme.container.poster),
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

@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_NO)
@Composable
private fun SetupInitialContentPreview() = PreviewLayout {
    SetupInitialContent(
        posters = List(10) { "" }.toImmutableList(),
        onContinueClick = {}
    )
}