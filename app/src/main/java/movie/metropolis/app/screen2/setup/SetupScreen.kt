@file:OptIn(ExperimentalMaterial3Api::class)

package movie.metropolis.app.screen2.setup

import android.content.res.Configuration.UI_MODE_NIGHT_NO
import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.draw.*
import androidx.compose.ui.res.*
import androidx.compose.ui.text.style.*
import androidx.compose.ui.tooling.preview.*
import androidx.compose.ui.unit.*
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList
import movie.core.model.Region
import movie.metropolis.app.R
import movie.metropolis.app.model.RegionView
import movie.metropolis.app.model.adapter.RegionViewFromFeature
import movie.metropolis.app.screen2.setup.component.RegionRow
import movie.metropolis.app.screen2.setup.component.SetupContainerColumn
import movie.metropolis.app.screen2.setup.component.SetupPreviewLayout
import movie.style.AppButton
import movie.style.Image
import movie.style.layout.PreviewLayout
import movie.style.modifier.glow
import movie.style.modifier.surface
import movie.style.rememberPaletteImageState
import movie.style.theme.Theme
import java.util.Locale

@Composable
fun SetupScreen(
    state: SetupState,
    posters: ImmutableList<String>,
    regions: ImmutableList<RegionView>,
    onStateChange: (SetupState) -> Unit,
    onRegionClick: (RegionView) -> Unit,
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

            SetupState.RegionSelection -> SetupRegionSelectionContent(
                modifier = modifier,
                regions = regions,
                posters = posters,
                onRegionClick = onRegionClick
            )
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

@Composable
private fun SetupRegionSelectionContent(
    posters: ImmutableList<String>,
    regions: ImmutableList<RegionView>,
    onRegionClick: (RegionView) -> Unit,
    modifier: Modifier = Modifier
) = Box(modifier = modifier.fillMaxSize(), propagateMinConstraints = true) {
    SetupPreviewLayout(
        modifier = Modifier.alpha(.3f),
        count = posters.size,
        contentPadding = PaddingValues(24.dp),
        rowCount = 5
    ) { it, selected ->
        val state = rememberPaletteImageState(url = posters[it])
        val elevation by animateDpAsState(if (selected) 16.dp else 0.dp, tween(700))
        val scale by animateFloatAsState(if (selected) 1.5f else 1f, tween(700))
        val color = state.palette.color
        Image(
            modifier = Modifier
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
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterVertically),
        horizontalAlignment = Alignment.CenterHorizontally,
        contentPadding = PaddingValues(24.dp)
    ) {
        items(regions) {
            val state = rememberPaletteImageState(url = it.icon)
            RegionRow(
                icon = { Image(state = state) },
                label = { Text(it.name) },
                onClick = { onRegionClick(it) },
                color = state.palette.color
            )
        }
        item { Spacer(modifier = Modifier.height(16.dp)) }
        item {
            Text(
                text = stringResource(R.string.select_country_description),
                textAlign = TextAlign.Center
            )
        }
    }
}

@Preview(showBackground = true, uiMode = UI_MODE_NIGHT_YES)
@Preview(showBackground = true, uiMode = UI_MODE_NIGHT_NO)
@Composable
private fun SetupScreenPreview(
    @PreviewParameter(SetupScreenParameter::class)
    parameter: SetupScreenParameter.Data
) = PreviewLayout {
    val regions = buildList {
        this += RegionViewFromFeature(Region.Czechia, Locale("cs", "CZ"))
        this += RegionViewFromFeature(Region.Slovakia, Locale("sk", "SK"))
        this += RegionViewFromFeature(Region.Poland, Locale("pl", "PL"))
        this += RegionViewFromFeature(Region.Hungary, Locale("hu", "HU"))
        this += RegionViewFromFeature(Region.Romania, Locale("ro", "RO"))
    }.toImmutableList()
    SetupScreen(
        state = parameter.state,
        regions = regions,
        posters = List(20) { "" }.toImmutableList(),
        onStateChange = {},
        onRegionClick = {}
    )
}

private class SetupScreenParameter : PreviewParameterProvider<SetupScreenParameter.Data> {
    override val values = sequence {
        yield(Data(SetupState.Initial))
        yield(Data(SetupState.RegionSelection))
    }

    class Data(val state: SetupState)
}